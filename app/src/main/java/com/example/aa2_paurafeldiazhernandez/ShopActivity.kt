package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.FortniteApiInstance
import FortniteApi.FortniteShopResponse
import FortniteApi.ShopEntry
import com.google.firebase.analytics.FirebaseAnalytics
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Fragment que muestra la tienda de Fortnite con búsqueda integrada
class ShopActivity : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShopAdapter
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var editSearch: EditText
    private lateinit var progressBar: ProgressBar

    private var allShopItems: List<ShopEntry> = emptyList()
    private var filteredItems: List<ShopEntry> = emptyList()
    private var currentMenu: Menu? = null

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
        editSearch = view.findViewById(R.id.edit_search)
        progressBar = view.findViewById(R.id.progressBar)

        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ShopAdapter(emptyList())
        recyclerView.adapter = adapter

        recyclerView.visibility = View.GONE

        editSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                filterItems(query)
            }
        })

        fetchShop()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shop_sort, menu)
        currentMenu = menu
        updateMenuIcon(R.id.sort_by_date, false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_date -> {
                val isAscending = adapter.sortBy(ShopAdapter.SortType.DATE)
                updateMenuIcon(R.id.sort_by_date, isAscending)
                logSortEvent("date", isAscending)
                true
            }
            R.id.sort_by_rarity -> {
                val isAscending = adapter.sortBy(ShopAdapter.SortType.RARITY)
                updateMenuIcon(R.id.sort_by_rarity, isAscending)
                logSortEvent("rarity", isAscending)
                true
            }
            R.id.sort_by_price -> {
                val isAscending = adapter.sortBy(ShopAdapter.SortType.PRICE)
                updateMenuIcon(R.id.sort_by_price, isAscending)
                logSortEvent("price", isAscending)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateMenuIcon(itemId: Int, isAscending: Boolean) {
        currentMenu?.findItem(itemId)?.icon = if (isAscending) {
            requireContext().getDrawable(android.R.drawable.arrow_up_float)
        } else {
            requireContext().getDrawable(android.R.drawable.arrow_down_float)
        }
    }


    // Registra eventos específicos de ordenamiento en Firebase Analytics
    private fun logSortEvent(sortType: String, isAscending: Boolean) {
        val direction = if (isAscending) "asc" else "desc"
        val eventName = "shop_sorted_${sortType}_${direction}"

        val bundle = Bundle().apply {
            putString("sort_type", sortType)
            putString("direction", direction)
            putLong("item_count", filteredItems.size.toLong())
        }

        firebaseAnalytics.logEvent(eventName, bundle)
    }

    // Obtiene la tienda usando el token de la API
    private fun fetchShop() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        val call = FortniteApiInstance.api.getShop(ApiConfig.FORTNITE_API_KEY, "en")

        call.enqueue(object : Callback<FortniteShopResponse> {
            override fun onResponse(
                call: Call<FortniteShopResponse>,
                response: Response<FortniteShopResponse>
            ) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val shopEntries = response.body()?.data?.entries?.filter { entry ->
                        val item = entry.brItems?.firstOrNull()
                        item?.images?.featured != null || item?.images?.icon != null
                    } ?: emptyList()

                    if (shopEntries.isNotEmpty()) {
                        allShopItems = shopEntries
                        filteredItems = shopEntries
                        adapter.updateShop(filteredItems)

                        Toast.makeText(
                            requireContext(),
                            "Loaded ${allShopItems.size} items",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Error loading shop: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<FortniteShopResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    "Connection error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun filterItems(query: String) {
        filteredItems = if (query.isEmpty()) {
            allShopItems
        } else {
            allShopItems.filter { entry ->
                val item = entry.brItems?.firstOrNull()
                item?.name?.contains(query, ignoreCase = true) == true ||
                        item?.type?.displayValue?.contains(query, ignoreCase = true) == true ||
                        item?.description?.contains(query, ignoreCase = true) == true
            }
        }

        adapter.updateShop(filteredItems)
    }
}