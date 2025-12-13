package com.example.aa2_paurafeldiazhernandez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.NewsItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class NewsAdapter(private var newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageNews)
        val titleView: TextView = view.findViewById(R.id.textTitle)
        val bodyView: TextView = view.findViewById(R.id.textBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        holder.titleView.text = newsItem.title
        holder.bodyView.text = newsItem.body

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(newsItem.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = newsList.size

    fun updateNews(newNews: List<NewsItem>) {
        newsList = newNews
        notifyDataSetChanged()
    }
}