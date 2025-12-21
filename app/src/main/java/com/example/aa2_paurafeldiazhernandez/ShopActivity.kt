package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.FortniteApiInstance
import FortniteApi.FortniteShopResponse
import com.google.firebase.analytics.FirebaseAnalytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


 // Fragmento que muestra la tienda de Fortnite
 // Obtiene los items disponibles desde la API y permite ordenarlos

class ShopActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var firebaseAnalytics : FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el menú de opciones para este fragmento
        // Para mostrar el menú en el toolbar
        setHasOptionsMenu(true)

    }

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
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
        // GridLayoutManager muestra los items en una cuadrícula de 2 columnas
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ShopAdapter(emptyList())
        recyclerView.adapter = adapter

        recyclerView.visibility = View.GONE

        fetchShop()
    }



     // Muestra las opciones para ordenar (fecha, rareza, precio)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shop_sort, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


     // Maneja los clicks en los items del menú
     // Ordena los items de la tienda según la opción seleccionada

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_date -> {
                val bundle = Bundle()
                bundle.putString("sort_type", "date")
                FirebaseAnalytics.getInstance(requireContext())
                    .logEvent("shop_sorted", bundle)
                adapter.sortBy(ShopAdapter.SortType.DATE)
                true
            }
            R.id.sort_by_rarity -> {
                val bundle = Bundle()
                bundle.putString("sort_type", "rarity")
                FirebaseAnalytics.getInstance(requireContext())
                    .logEvent("shop_sorted", bundle)
                adapter.sortBy(ShopAdapter.SortType.RARITY)
                true
            }
            R.id.sort_by_price -> {
                val bundle = Bundle()
                bundle.putString("sort_type", "price")
                FirebaseAnalytics.getInstance(requireContext())
                    .logEvent("shop_sorted", bundle)
                adapter.sortBy(ShopAdapter.SortType.PRICE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // Realiza una petición HTTP a la API de Fortnite para obtener la tienda

    private fun fetchShop() {
        val call = FortniteApiInstance.api.getShop("en")

        call.enqueue(object : Callback<FortniteShopResponse> {
            override fun onResponse(
                call: Call<FortniteShopResponse>,
                response: Response<FortniteShopResponse>
            ) {
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    // Extrae las entradas de la tienda del body de la respuesta
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