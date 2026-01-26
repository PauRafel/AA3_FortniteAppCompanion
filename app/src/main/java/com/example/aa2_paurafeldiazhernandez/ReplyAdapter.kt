package com.example.aa2_paurafeldiazhernandez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

// Adapter para mostrar la lista de respuestas

class ReplyAdapter(private val repliesList: MutableList<Reply>) :
    RecyclerView.Adapter<ReplyAdapter.ReplyViewHolder>() {

    class ReplyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEmail: TextView = view.findViewById(R.id.txt_reply_email)
        val txtContent: TextView = view.findViewById(R.id.txt_reply_content)
        val txtTimestamp: TextView = view.findViewById(R.id.txt_reply_timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reply, parent, false)
        return ReplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = repliesList[position]

        holder.txtEmail.text = reply.userEmail
        holder.txtContent.text = reply.content
        holder.txtTimestamp.text = formatTimestamp(reply.timestamp)
    }

    override fun getItemCount(): Int = repliesList.size

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestamp))
    }
}