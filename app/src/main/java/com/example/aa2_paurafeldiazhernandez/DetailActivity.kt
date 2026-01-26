package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


// Activity que muestra el detalle de un post específico
//Permite ver y añadir respuestas al post

class DetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var txtTitle: TextView
    private lateinit var txtContent: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtTimestamp: TextView
    private lateinit var txtNoReplies: TextView
    private lateinit var recyclerViewReplies: RecyclerView
    private lateinit var editReplyContent: EditText
    private lateinit var btnSendReply: Button

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var replyAdapter: ReplyAdapter

    private var postId: String? = null
    private var currentPost: Post? = null
    private val repliesList = mutableListOf<Reply>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        postId = intent.getStringExtra("POST_ID")

        initializeViews()
        setupToolbar()
        setupDatabase()
        setupRecyclerView()
        setupReplyButton()
        applyTheme()

        if (postId != null) {
            fetchPostDetail(postId!!)
            loadReplies(postId!!)
        } else {
            Toast.makeText(this, "Error: No post ID", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar_post_detail)
        progressBar = findViewById(R.id.progressBarPostDetail)
        txtTitle = findViewById(R.id.txt_detail_title)
        txtContent = findViewById(R.id.txt_detail_content)
        txtEmail = findViewById(R.id.txt_detail_email)
        txtTimestamp = findViewById(R.id.txt_detail_timestamp)
        txtNoReplies = findViewById(R.id.txt_no_replies)
        recyclerViewReplies = findViewById(R.id.recyclerViewReplies)
        editReplyContent = findViewById(R.id.edit_reply_content)
        btnSendReply = findViewById(R.id.btn_send_reply)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Post Detail"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupDatabase() {
        database = FirebaseDatabase.getInstance("https://fortniteappcompanionaa3-default-rtdb.europe-west1.firebasedatabase.app/")
            .reference
        auth = FirebaseAuth.getInstance()
    }

    private fun setupRecyclerView() {
        recyclerViewReplies.layoutManager = LinearLayoutManager(this)
        replyAdapter = ReplyAdapter(repliesList)
        recyclerViewReplies.adapter = replyAdapter
    }

    private fun setupReplyButton() {
        btnSendReply.setOnClickListener {
            sendReply()
        }
    }

    private fun applyTheme() {
        val primaryColor = ThemeManager.getPrimaryColor(this)
        toolbar.setBackgroundColor(primaryColor)
        btnSendReply.setBackgroundColor(primaryColor)
    }


     // Obtiene el detalle de un post específico usando su ID

    private fun fetchPostDetail(id: String) {
        showLoading(true)

        database.child("posts").child(id).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    showLoading(false)

                    val post = snapshot.getValue(Post::class.java)
                    if (post != null) {
                        currentPost = post
                        displayPostDetail(post)
                    } else {
                        Toast.makeText(this@DetailActivity, "Post not found", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading(false)
                    Toast.makeText(this@DetailActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        )
    }


    // Carga las respuestas del post en tiempo real

    private fun loadReplies(id: String) {
        database.child("replies").orderByChild("postId").equalTo(id)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val reply = snapshot.getValue(Reply::class.java)
                    reply?.let {
                        repliesList.add(it)
                        repliesList.sortByDescending { r -> r.timestamp }
                        replyAdapter.notifyDataSetChanged()
                        updateRepliesVisibility()
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    val reply = snapshot.getValue(Reply::class.java)
                    reply?.let { updatedReply ->
                        val index = repliesList.indexOfFirst { it.id == updatedReply.id }
                        if (index != -1) {
                            repliesList[index] = updatedReply
                            replyAdapter.notifyItemChanged(index)
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    val reply = snapshot.getValue(Reply::class.java)
                    reply?.let { removedReply ->
                        val index = repliesList.indexOfFirst { it.id == removedReply.id }
                        if (index != -1) {
                            repliesList.removeAt(index)
                            replyAdapter.notifyItemRemoved(index)
                            updateRepliesVisibility()
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DetailActivity, "Error loading replies: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


      // Envía una nueva respuesta al post

    private fun sendReply() {
        val content = editReplyContent.text.toString()

        if (content.isEmpty()) {
            editReplyContent.error = "Reply cannot be empty"
            return
        }

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "You must be logged in to reply", Toast.LENGTH_SHORT).show()
            return
        }

        if (postId == null) {
            Toast.makeText(this, "Error: Invalid post", Toast.LENGTH_SHORT).show()
            return
        }

        btnSendReply.isEnabled = false

        val replyId = database.child("replies").push().key
        if (replyId == null) {
            btnSendReply.isEnabled = true
            Toast.makeText(this, "Error generating reply ID", Toast.LENGTH_SHORT).show()
            return
        }

        val reply = Reply(
            id = replyId,
            postId = postId!!,
            userEmail = currentUser.email ?: "",
            content = content,
            timestamp = System.currentTimeMillis()
        )

        database.child("replies").child(replyId).setValue(reply)
            .addOnSuccessListener {
                editReplyContent.text.clear()
                btnSendReply.isEnabled = true
                Toast.makeText(this, "Reply sent!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                btnSendReply.isEnabled = true
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


     // Muestra toda la información del post

    private fun displayPostDetail(post: Post) {
        txtTitle.text = post.title
        txtContent.text = post.content
        txtEmail.text = "Email: ${post.userEmail}"
        txtTimestamp.text = "Posted: ${formatTimestamp(post.timestamp)}"
    }

    private fun updateRepliesVisibility() {
        if (repliesList.isEmpty()) {
            txtNoReplies.visibility = View.VISIBLE
            recyclerViewReplies.visibility = View.GONE
        } else {
            txtNoReplies.visibility = View.GONE
            recyclerViewReplies.visibility = View.VISIBLE
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        txtTitle.visibility = if (show) View.GONE else View.VISIBLE
        txtContent.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp))
    }
}