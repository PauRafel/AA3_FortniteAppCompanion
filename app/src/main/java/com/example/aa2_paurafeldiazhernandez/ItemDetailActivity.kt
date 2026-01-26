package com.example.aa2_paurafeldiazhernandez

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import FortniteApi.FortniteApiInstance
import FortniteApi.ItemDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


 //Activity que muestra el detalle completo de un item de Fortnite
 // Recibe el ID del item y hace una llamada específica a la API para obtener información detallada

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollContent: View

    private lateinit var imgItemFeatured: ImageView
    private lateinit var txtItemName: TextView
    private lateinit var txtItemDescription: TextView
    private lateinit var txtItemType: TextView
    private lateinit var txtItemRarity: TextView
    private lateinit var txtItemSeries: TextView
    private lateinit var txtItemSet: TextView
    private lateinit var txtItemIntroduction: TextView
    private lateinit var txtItemPrice: TextView

    private var itemId: String? = null
    private var itemPrice: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        // Recibe el ID del item desde el Intent
        itemId = intent.getStringExtra("ITEM_ID")
        itemPrice = intent.getIntExtra("ITEM_PRICE", 0)

        initializeViews()
        setupToolbar()
        applyTheme()

        if (itemId != null) {
            fetchItemDetail(itemId!!)
        } else {
            finish()
        }
    }

    private fun initializeViews() {
        toolbar = findViewById(R.id.toolbar_detail)
        progressBar = findViewById(R.id.progressBarDetail)
        scrollContent = findViewById(R.id.scroll_content)

        imgItemFeatured = findViewById(R.id.img_item_featured)
        txtItemName = findViewById(R.id.txt_item_name)
        txtItemDescription = findViewById(R.id.txt_item_description)
        txtItemType = findViewById(R.id.txt_item_type)
        txtItemRarity = findViewById(R.id.txt_item_rarity)
        txtItemSeries = findViewById(R.id.txt_item_series)
        txtItemSet = findViewById(R.id.txt_item_set)
        txtItemIntroduction = findViewById(R.id.txt_item_introduction)
        txtItemPrice = findViewById(R.id.txt_item_price)

    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = "Item Detail"

        toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun applyTheme() {
        val primaryColor = ThemeManager.getPrimaryColor(this)
        toolbar.setBackgroundColor(primaryColor)
    }

    // Realiza la llamada a la API para obtener el detalle completo del item
    private fun fetchItemDetail(id: String) {
        progressBar.visibility = View.VISIBLE
        scrollContent.visibility = View.GONE

        val call = FortniteApiInstance.api.getItemById(ApiConfig.FORTNITE_API_KEY, id)

        call.enqueue(object : Callback<ItemDetailResponse> {
            override fun onResponse(
                call: Call<ItemDetailResponse>,
                response: Response<ItemDetailResponse>
            ) {
                progressBar.visibility = View.GONE
                scrollContent.visibility = View.VISIBLE

                if (response.isSuccessful) {
                    val item = response.body()?.data
                    if (item != null) {
                        displayItemDetail(item)
                    } else {
                        Toast.makeText(
                            this@ItemDetailActivity,
                            "Item not found",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(
                        this@ItemDetailActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ItemDetailResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                    this@ItemDetailActivity,
                    "Connection error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
    }


     //Muestra toda la información detallada del item en la interfaz

    private fun displayItemDetail(item: FortniteApi.BrItem) {
        // Nombre y descripción
        txtItemName.text = item.name
        txtItemDescription.text = item.description

        // Tipo de item
        txtItemType.text = "Type: ${item.type.displayValue}"

        // Rareza con color
        val rarityText = "Rarity: ${item.rarity.displayValue}"
        txtItemRarity.text = rarityText
        val rarityColor = getRarityColor(item.rarity.value)
        txtItemRarity.setTextColor(ContextCompat.getColor(this, rarityColor))

        // Serie (si existe)
        if (item.series != null) {
            txtItemSeries.visibility = View.VISIBLE
            txtItemSeries.text = "Series: ${item.series.value}"
        } else {
            txtItemSeries.visibility = View.GONE
        }

        // Set/Conjunto (si existe)
        if (item.set != null) {
            txtItemSet.visibility = View.VISIBLE
            txtItemSet.text = "Set: ${item.set.text}"
        } else {
            txtItemSet.visibility = View.GONE
        }

        // Introducción (capítulo y temporada)
        if (item.introduction != null) {
            txtItemIntroduction.visibility = View.VISIBLE
            txtItemIntroduction.text = "${item.introduction.text}"
        } else {
            txtItemIntroduction.visibility = View.GONE
        }

        // Precio
        if (itemPrice > 0) {
            txtItemPrice.text = itemPrice.toString()
        } else {
            txtItemPrice.text = "N/A"
        }

        // Imagen principal
        val imageUrl = item.images.featured ?: ""

        if (imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(imageUrl)
                .into(imgItemFeatured)

            // Aplica el color de rareza como fondo
            val rarityColorInt = ContextCompat.getColor(this, rarityColor)
            imgItemFeatured.setBackgroundColor(rarityColorInt)
        }
    }

    private fun getRarityColor(rarity: String): Int {
        return when (rarity.lowercase()) {
            "common" -> R.color.rarity_common
            "uncommon" -> R.color.rarity_uncommon
            "rare" -> R.color.rarity_rare
            "epic" -> R.color.rarity_epic
            "legendary" -> R.color.rarity_legendary
            "mythic" -> R.color.rarity_mythic
            "exotic" -> R.color.rarity_exotic
            "transcendent" -> R.color.rarity_transcendent
            "marvel" -> R.color.rarity_marvel
            "dc", "gaminglegends", "icon", "starwars",
            "frozen", "lava", "shadow", "dark" -> R.color.rarity_series
            else -> R.color.rarity_default
        }
    }
}