package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.FortniteApiInstance
import FortniteApi.FortniteResponse
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


 // Fragmento que muestra las noticias de Fortnite
 //Obtiene los datos desde la API de Fortnite y los muestra en un RecyclerView

class NewsActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewNews)

        // LinearLayoutManager muestra los items en una lista vertical
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Inicializa el adapter con una lista vacía hasta que lleguen los datos de la API
        adapter = NewsAdapter(emptyList())
        recyclerView.adapter = adapter

        // Oculta el RecyclerView mientras se cargan los datos
        recyclerView.visibility = View.GONE

        fetchFortniteNews()
    }


     //Realiza una petición HTTP a la API de Fortnite para obtener las noticias

    private fun fetchFortniteNews() {
        // Obtiene la instancia singleton de la API y hace la petición
        val call = FortniteApiInstance.api.getNews("en")

        call.enqueue(object : Callback<FortniteResponse> {

            override fun onResponse(
                call: Call<FortniteResponse>,
                response: Response<FortniteResponse>
            ) {
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    // Extrae las noticias del body de la respuesta
                    // Devuelve una lista vacía si data o motds son null
                    val newsItems = response.body()?.data?.motds ?: emptyList()

                    // Filtra solo las noticias que no están marcadas como ocultas
                    // La API puede devolver noticias ocultas que no deben mostrarse
                    val visibleNews = newsItems.filter { !it.hidden }

                    if (visibleNews.isNotEmpty()) {
                        adapter.updateNews(visibleNews)
                    }
                }
            }

            override fun onFailure(call: Call<FortniteResponse>, t: Throwable) {
                recyclerView.visibility = View.VISIBLE
            }
        })
    }
}