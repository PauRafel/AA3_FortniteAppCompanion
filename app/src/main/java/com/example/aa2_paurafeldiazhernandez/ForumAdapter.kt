package com.example.aa2_paurafeldiazhernandez

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

 // Adapter para mostrar la lista de posts del foro

class ForumAdapter(private val postsList: MutableList<Post>) :
    RecyclerView.Adapter<ForumAdapter.ForumViewHolder>() {

    class ForumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.card_post)
        val txtTitle: TextView = view.findViewById(R.id.txt_post_title)
        val txtTimestamp: TextView = view.findViewById(R.id.txt_post_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_post, parent, false)
        return ForumViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForumViewHolder, position: Int) {
        val post = postsList[position]

        holder.txtTitle.text = post.title
        holder.txtTimestamp.text = formatTimestamp(post.timestamp)

        // Listener para abrir el detalle del post
        holder.cardView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("POST_ID", post.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = postsList.size

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp))
    }
}