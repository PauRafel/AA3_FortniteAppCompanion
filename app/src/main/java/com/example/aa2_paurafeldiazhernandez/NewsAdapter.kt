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


// Adapter para el RecyclerView de noticias
// Vincula los datos de NewsItem con las vistas del layout item_news

class NewsAdapter(private var newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

        // ViewHolder que contiene las referencias a las vistas de cada item
    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageNews)
        val titleView: TextView = view.findViewById(R.id.textTitle)
        val bodyView: TextView = view.findViewById(R.id.textBody)
    }


    // Crea nuevas vistas cuando el RecyclerView las necesita
    // Infla el layout de cada item individual
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }


    // Vincula los datos de una noticia específica con las vistas del ViewHolder
    // Se llama cada vez que un item necesita mostrarse en pantalla
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]

        holder.titleView.text = newsItem.title
        holder.bodyView.text = newsItem.body

        // Utilizamos Glide (librería) para cargar imágenes desde URLs
        Glide.with(holder.itemView.context)
            .load(newsItem.image)
            .into(holder.imageView)
    }


    override fun getItemCount(): Int = newsList.size


    // Actualiza la lista de noticias y notifica al RecyclerView que los datos han cambiado para que se redibuje
    fun updateNews(newNews: List<NewsItem>) {
        newsList = newNews
        notifyDataSetChanged() // Redibuja todos los items visibles
    }
}