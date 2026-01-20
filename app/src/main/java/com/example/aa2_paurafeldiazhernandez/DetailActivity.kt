package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


 // Activity que muestra en detalle un post específico

class DetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var txtTitle: TextView
    private lateinit var txtContent: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtTimestamp: TextView

    private lateinit var database: DatabaseReference

    private var postId: String? = null
    private var currentPost: Post? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        postId = intent.getStringExtra("POST_ID")

        initializeViews()
        setupToolbar()
        setupDatabase()
        applyTheme()

        if (postId != null) {
            fetchPostDetail(postId!!)
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
    }

    private fun applyTheme() {
        val primaryColor = ThemeManager.getPrimaryColor(this)
        toolbar.setBackgroundColor(primaryColor)
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
                        Toast.makeText(
                            this@DetailActivity,
                            "Post not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoading(false)
                    Toast.makeText(
                        this@DetailActivity,
                        "Error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        )
    }


     //Muestra toda la información del post

    private fun displayPostDetail(post: Post) {
        txtTitle.text = post.title
        txtContent.text = post.content
        txtEmail.text = "Email: ${post.userEmail}"
        txtTimestamp.text = "Posted: ${formatTimestamp(post.timestamp)}"
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