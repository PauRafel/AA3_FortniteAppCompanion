package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.FortniteApiInstance
import FortniteApi.FortniteShopResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ShopAdapter(emptyList())
        recyclerView.adapter = adapter

        recyclerView.visibility = View.GONE

        fetchShop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shop_sort, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_date -> {
                adapter.sortBy(ShopAdapter.SortType.DATE)
                true
            }
            R.id.sort_by_rarity -> {
                adapter.sortBy(ShopAdapter.SortType.RARITY)
                true
            }
            R.id.sort_by_price -> {
                adapter.sortBy(ShopAdapter.SortType.PRICE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchShop() {
        val call = FortniteApiInstance.api.getShop("en")

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