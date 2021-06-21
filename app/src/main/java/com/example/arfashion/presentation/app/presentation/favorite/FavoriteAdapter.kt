package com.example.arfashion.presentation.app.presentation.favorite

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.favorite.FavoriteItem
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.detail.ProductDetailActivity
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.Utils

class FavoriteAdapter(private val context: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private var favoriteList: MutableList<FavoriteItem> = mutableListOf()

    var deleteProductClickEvent: ((product: FavoriteItem, position: Int) -> Unit)? = null

    fun setFavoriteList(favoriteList: List<FavoriteItem>?) {
        this.favoriteList.clear()
        favoriteList?.let {
            this.favoriteList.addAll(favoriteList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return favoriteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(favoriteList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.favorite_item_name)
        private val image = view.findViewById<ImageView>(R.id.favorite_item_image)
        private val priceSale = view.findViewById<TextView>(R.id.favorite_item_priceSale)
        private val defaultPrice = view.findViewById<TextView>(R.id.favorite_item_defaultPrice)
        private val rating = view.findViewById<RatingBar>(R.id.favorite_item_rating)
        private val totalComments = view.findViewById<TextView>(R.id.favorite_item_totalRating)
        private val favoriteIcon = view.findViewById<ImageView>(R.id.favorite_icon)

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra(KEY_PRODUCT_ID, favoriteList[adapterPosition].id)
                context.startActivity(intent)
            }

            favoriteIcon.setOnClickListener {
                deleteProductClickEvent?.invoke(favoriteList[adapterPosition], adapterPosition)
            }
        }


        @SuppressLint("SetTextI18n", "StringFormatMatches")
        fun bindData(res: FavoriteItem) {
            name.text = res.name
            if(res.priceSale == res.price){
                //only show priceSale
                priceSale.text =  Utils.formatPrice(res.priceSale) + "VND"
                defaultPrice.visibility = View.GONE
            }else{
                //show both
                priceSale.text =  Utils.formatPrice(res.priceSale) + "VND"
                context.getString(R.string.default_price, Utils.formatPrice(res.price)) + "VND"
            }
            Glide.with(image)
                .load(res.images[0].url)
                .into(image)
            rating.rating = res.star.toFloat()
            totalComments.text = context.getString(
                R.string.total_rating,
                res.total_comments
            )
        }
    }
}

