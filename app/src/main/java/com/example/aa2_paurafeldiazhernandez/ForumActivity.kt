package com.example.aa2_paurafeldiazhernandez

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*


  //Fragment que muestra el foro
  //Usa Realtime Database para mostrar posts en tiempo real

class ForumActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ForumAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var fabCreatePost: FloatingActionButton
    private lateinit var txtEmptyForum: TextView

    private lateinit var database: DatabaseReference

    private var postsList = mutableListOf<Post>()
    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_forum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeViews(view)
        setupRecyclerView()
        setupDatabase()
        setupFab()
    }

    private fun initializeViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewForum)
        progressBar = view.findViewById(R.id.progressBarForum)
        fabCreatePost = view.findViewById(R.id.fab_create_post)
        txtEmptyForum = view.findViewById(R.id.txt_empty_forum)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ForumAdapter(postsList)
        recyclerView.adapter = adapter
    }


     //Configura la conexión con Realtime Database

    private fun setupDatabase() {
        // Obtener referencia a la base de datos CON URL
        database = FirebaseDatabase.getInstance("https://fortniteappcompanionaa3-default-rtdb.europe-west1.firebasedatabase.app/")
            .reference.child("posts")

        // Mostrar loading mientras se cargan los datos
        showLoading(true)

        // Listener para verificar si hay datos
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ForumActivity", "Datos recibidos: ${snapshot.childrenCount} posts")

                if (isFirstLoad) {
                    isFirstLoad = false
                    showLoading(false)

                    if (!snapshot.exists() || snapshot.childrenCount == 0L) {
                        updateEmptyState()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showLoading(false)
                updateEmptyState()
            }
        })

        // Listener en tiempo real, se ejecuta cada vez que hay cambios
        database.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post = snapshot.getValue(Post::class.java)
                post?.let {
                    postsList.add(0, it) // Añadir al principio de la lista
                    adapter.notifyItemInserted(0)
                    recyclerView.scrollToPosition(0)

                    if (isFirstLoad) {
                        isFirstLoad = false
                        showLoading(false)
                    }

                    updateEmptyState()
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val post = snapshot.getValue(Post::class.java)
                post?.let { updatedPost ->
                    val index = postsList.indexOfFirst { it.id == updatedPost.id }
                    if (index != -1) {
                        postsList[index] = updatedPost
                        adapter.notifyItemChanged(index)
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val post = snapshot.getValue(Post::class.java)
                post?.let { removedPost ->
                    val index = postsList.indexOfFirst { it.id == removedPost.id }
                    if (index != -1) {
                        postsList.removeAt(index)
                        adapter.notifyItemRemoved(index)
                        updateEmptyState()
                    }
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                showLoading(false)
                updateEmptyState()
            }
        })
    }

    private fun setupFab() {
        fabCreatePost.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }

        val primaryColor = ThemeManager.getPrimaryColor(requireContext())
        fabCreatePost.backgroundTintList =
            android.content.res.ColorStateList.valueOf(primaryColor)
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE

        if (!show) {
            updateEmptyState()
        }
    }

    private fun updateEmptyState() {
        if (postsList.isEmpty()) {
            txtEmptyForum.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            txtEmptyForum.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
}