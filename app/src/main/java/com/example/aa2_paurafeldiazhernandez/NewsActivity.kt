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

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NewsAdapter(emptyList())
        recyclerView.adapter = adapter

        recyclerView.visibility = View.GONE


        fetchFortniteNews()
    }

    private fun fetchFortniteNews() {
        val call = FortniteApiInstance.api.getNews("en")

        call.enqueue(object : Callback<FortniteResponse> {
            override fun onResponse(
                call: Call<FortniteResponse>,
                response: Response<FortniteResponse>
            ) {
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val newsItems = response.body()?.data?.motds ?: emptyList()

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