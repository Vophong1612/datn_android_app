package com.example.arfashion.presentation.app.presentation.bill

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.product.comment.rating.KEY_RATING_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.comment.rating.KEY_RATING_PRODUCT_IMAGE
import com.example.arfashion.presentation.app.presentation.product.comment.rating.KEY_RATING_PRODUCT_NAME
import com.example.arfashion.presentation.app.presentation.product.comment.rating.RatingActivity
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.Utils.Companion.standardFormat

class ProductsInDetailBillAdapter(private val context: Activity) :
    RecyclerView.Adapter<ProductsInDetailBillAdapter.ViewHolder>() {

    private var productList: MutableList<Product> = mutableListOf()

    var productClickLister: ((productId: String) -> Unit)? = null

    fun setProductList(productList: List<Product>?) {
        this.productList.clear()
        productList?.let {
            this.productList.addAll(productList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_payment_detail, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(productList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.item_name)
        private val image = view.findViewById<ImageView>(R.id.item_image_product)
        private val color = view.findViewById<TextView>(R.id.item_color)
        private val size = view.findViewById<TextView>(R.id.item_size)
        private val price = view.findViewById<TextView>(R.id.item_price)
        private val totalProduct = view.findViewById<TextView>(R.id.item_totalProduct)
        private val rating = view.findViewById<TextView>(R.id.item_rating)

        init {
            rating.setOnClickListener {
                val intent = Intent(context, RatingActivity::class.java)
                intent.putExtra(KEY_RATING_PRODUCT_ID, productList[adapterPosition].id)
                intent.putExtra(KEY_RATING_PRODUCT_NAME, productList[adapterPosition].name)
                intent.putExtra(KEY_RATING_PRODUCT_IMAGE, productList[adapterPosition].images[0])
                context.startActivity(intent)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(res: Product) {
            name.text = res.name
            if (res.colors.isNotEmpty()) {
                color.text = context.getString(R.string.cart_color, res.colors[0])
            }
            if (res.sizes.isNotEmpty()) {
                size.text = context.getString(R.string.cart_size, res.sizes[0].name)
            }
            totalProduct.text = "x " + res.total
            price.text = context.getString(R.string.price, res.prices.standardFormat())
            Glide.with(image)
                .load(res.images[0])
                .into(image)

        }
    }
}