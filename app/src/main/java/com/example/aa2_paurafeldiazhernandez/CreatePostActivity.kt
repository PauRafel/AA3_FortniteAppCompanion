package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


 // Activity para crear un nuevo post en el foro

class CreatePostActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var editTitle: EditText
    private lateinit var editContent: EditText
    private lateinit var btnPublish: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        initializeViews()
        setupToolbar()
        setupDatabase()
        setupPublishButton()
        applyTheme()
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar_create_post)
        editTitle = findViewById(R.id.edit_post_title)
        editContent = findViewById(R.id.edit_post_content)
        btnPublish = findViewById(R.id.btn_publish_post)
        progressBar = findViewById(R.id.progressBarCreatePost)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Create Post"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupDatabase() {
        database = FirebaseDatabase.getInstance("https://fortniteappcompanionaa3-default-rtdb.europe-west1.firebasedatabase.app/")
            .reference
        auth = FirebaseAuth.getInstance()
    }

    private fun setupPublishButton() {
        btnPublish.setOnClickListener {
            publishPost()
        }
    }

    private fun applyTheme() {
        val primaryColor = ThemeManager.getPrimaryColor(this)
        toolbar.setBackgroundColor(primaryColor)
        btnPublish.setBackgroundColor(primaryColor)
    }

    /**
     * Publica un nuevo post en Firebase
     * Genera un ID único y almacena todos los datos del post
     */
    private fun publishPost() {
        val title = editTitle.text.toString().trim()
        val content = editContent.text.toString().trim()

        // Validar campos
        if (title.isEmpty()) {
            editTitle.error = "Title is required"
            return
        }

        if (content.isEmpty()) {
            editContent.error = "Content is required"
            return
        }

        showLoading(true)

        // Obtener usuario actual
        val currentUser = auth.currentUser
        if (currentUser == null) {
            showLoading(false)
            Toast.makeText(this, "You must be logged in", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Generar ID único para el post
        val postId = database.child("posts").push().key
        if (postId == null) {
            showLoading(false)
            Toast.makeText(this, "Error generating post ID", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el objeto Post
        val post = Post(
            id = postId,
            userEmail = currentUser.email ?: "",
            title = title,
            content = content,
            timestamp = System.currentTimeMillis(),
        )

        // Insertar en Firebase
        database.child("posts").child(postId).setValue(post)
            .addOnSuccessListener {
                showLoading(false)
                Toast.makeText(this, "Post published!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                showLoading(false)
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        btnPublish.isEnabled = !show
        editTitle.isEnabled = !show
        editContent.isEnabled = !show
    }
}