package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.FortniteApiInstance
import FortniteApi.FortniteShopResponse
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_shop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewShop)


        recyclerView.layoutManager = GridLayoutManager(requireContext(),2 )
        adapter = ShopAdapter(emptyList())
        recyclerView.adapter = adapter


        recyclerView.visibility = View.GONE


        fetchShop()
    }

    private fun fetchShop() {
        val call = FortniteApiInstance.api.getShop("es")

        call.enqueue(object : Callback<FortniteShopResponse> {
            override fun onResponse(
                call: Call<FortniteShopResponse>,
                response: Response<FortniteShopResponse>
            ) {
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val shopEntries = response.body()?.data?.entries ?: emptyList()

                    if (shopEntries.isNotEmpty()) {
                        adapter.updateShop(shopEntries)
                    }

                }
            }

            override fun onFailure(call: Call<FortniteShopResponse>, t: Throwable) {
                recyclerView.visibility = View.VISIBLE
            }
        })
    }
}