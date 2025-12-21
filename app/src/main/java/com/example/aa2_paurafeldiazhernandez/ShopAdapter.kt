package com.example.aa2_paurafeldiazhernandez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.ShopEntry
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*


 // Adapter para el RecyclerView de la tienda
 // Maneja la visualización de items y permite ordenarlos por diferentes criterios

class ShopAdapter(private var shopList: List<ShopEntry>) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    // Lista completa de items, usada como fuente para ordenar
    private var allItems: List<ShopEntry> = emptyList()

    enum class SortType {
        DATE, RARITY, PRICE
    }


    // ViewHolder que contiene las referencias a las vistas de cada item de la tienda
    class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageShopItem)
        val nameView: TextView = view.findViewById(R.id.textItemName)
        val priceView: TextView = view.findViewById(R.id.textPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }


     // Vincula los datos de un item de la tienda con las vistas

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopEntry = shopList[position]

        // Los items de Fortnite pueden tener múltiples objetos, tomamos el primero
        val item = shopEntry.brItems?.firstOrNull()

        // Usa el nombre del item
        holder.nameView.text = item?.name
        holder.priceView.text = "${shopEntry.finalPrice}"

        val rarityValue = item?.rarity?.value ?: "common"

        // Aplica el color de fondo según la rareza del item
        // Cada rareza tiene un color distintivo (común=gris, legendario=naranja, etc.)
        val rarityColorRes = getRarityColor(rarityValue)
        val rarityColor = ContextCompat.getColor(holder.itemView.context, rarityColorRes)
        holder.imageView.setBackgroundColor(rarityColor)

        val imageUrl = item?.images?.featured
            ?: item?.images?.icon
            ?: shopEntry.newDisplayAsset?.images?.get("")
            ?: shopEntry.newDisplayAsset?.images?.get("")
            ?: shopEntry.bundle?.image
            ?: ""

        if (imageUrl.isNotEmpty()) {
            // Glide carga la imagen desde la URL
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .into(holder.imageView)
        }
    }

    override fun getItemCount(): Int = shopList.size


    // Actualiza la lista completa de items de la tienda

    fun updateShop(newShop: List<ShopEntry>) {
        allItems = newShop.filter { entry ->
            val item = entry.brItems?.firstOrNull()
            item?.images?.featured != null
        }
        // Ordena por fecha por defecto al cargar la tienda
        sortBy(SortType.DATE)
    }


    //  Ordena los items según el tipo especificado
    //  sortedByDescending ordena de mayor a menor (más reciente/raro/caro primero)

    fun sortBy(sortType: SortType) {
        shopList = when (sortType) {
            SortType.DATE -> {
                // Ordena por fecha de entrada a la tienda
                allItems.sortedByDescending { entry ->
                    entry.inDate
                }
            }
            SortType.RARITY -> {
                // Ordena por rareza usando valores numéricos
                allItems.sortedByDescending { entry ->
                    val rarityValue = entry.brItems?.firstOrNull()?.rarity?.value ?: "common"
                    getRarityOrder(rarityValue)
                }
            }
            SortType.PRICE -> {
                // Ordena por precio en V-Bucks
                allItems.sortedByDescending { entry ->
                    entry.finalPrice
                }
            }
        }
        notifyDataSetChanged() // Notifica al RecyclerView que los datos cambiaron
    }


    //  Asigna un valor numérico a cada rareza para poder ordenar
    //  Mayor número = mayor rareza (Mythic es lo más raro)

    private fun getRarityOrder(rarity: String): Int {
        return when (rarity.lowercase()) {
            "mythic" -> 7
            "exotic" -> 6
            "legendary" -> 5
            "epic" -> 4
            "rare" -> 3
            "uncommon" -> 2
            "common" -> 1
            else -> 0
        }
    }


    //  Retorna el recurso de color correspondiente a cada rareza

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