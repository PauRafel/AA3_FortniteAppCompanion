package com.example.aa2_paurafeldiazhernandez

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.ShopEntry
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class ShopAdapter(private var shopList: List<ShopEntry>) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

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

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopEntry = shopList[position]

        val item = shopEntry.brItems?.firstOrNull()

        holder.nameView.text = item?.name ?: shopEntry.devName

        holder.priceView.text = "${shopEntry.finalPrice}"

        val rarityValue = item?.rarity?.value ?: "common"

        val rarityColorRes = getRarityColor(rarityValue)
        val rarityColor = ContextCompat.getColor(holder.itemView.context, rarityColorRes)
        holder.imageView.setBackgroundColor(rarityColor)

        val imageUrl = item?.images?.icon
            ?: item?.images?.featured
            ?: shopEntry.newDisplayAsset?.images?.get("Background")
            ?: ""

        if (imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int = shopList.size

    fun updateShop(newShop: List<ShopEntry>) {
        shopList = newShop.filter { entry ->
            val item = entry.brItems?.firstOrNull()
            val hasImage = item?.images?.featured != null
            hasImage
        }
        notifyDataSetChanged()
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