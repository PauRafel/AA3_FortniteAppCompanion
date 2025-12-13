package com.example.aa2_paurafeldiazhernandez

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import FortniteApi.ShopEntry
import com.bumptech.glide.Glide

class ShopAdapter(private var shopList: List<ShopEntry>) :
    RecyclerView.Adapter<ShopAdapter.ShopViewHolder>() {

    class ShopViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageShopItem)
        val nameView: TextView = view.findViewById(R.id.textItemName)
        val priceView: TextView = view.findViewById(R.id.textPrice)
        val rarityView: TextView = view.findViewById(R.id.textRarity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_shop, parent, false)
        return ShopViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shopEntry = shopList[position]

        // Obtener el primer item del bundle
        val item = shopEntry.brItems?.firstOrNull()

        // Nombre del item
        holder.nameView.text = item?.name ?: shopEntry.devName

        // Precio
        holder.priceView.text = "${shopEntry.finalPrice} V-Bucks"

        // Raridad
        holder.rarityView.text = item?.rarity?.displayValue ?: "Unknown"

        // Cargar imagen
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
        shopList = newShop
        notifyDataSetChanged()
    }
}