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
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        recyclerView = findViewById(R.id.recyclerViewShop)


        recyclerView.layoutManager = GridLayoutManager(this, 2)
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

                    if (shopEntries.isEmpty()) {
                        Toast.makeText(
                            this@ShopActivity,
                            "No hay items en la tienda",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        adapter.updateShop(shopEntries)

                        Toast.makeText(
                            this@ShopActivity,
                            "${shopEntries.size} items cargados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ShopActivity,
                        "Error al cargar tienda: ${response.code()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<FortniteShopResponse>, t: Throwable) {
                recyclerView.visibility = View.VISIBLE

                Toast.makeText(
                    this@ShopActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}