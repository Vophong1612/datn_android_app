package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Product

class ProductAdapter(private val context: Context) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()

    fun setProducts(products: List<Product>?) {
        this.products.clear()
        products?.let {
            this.products.addAll(products)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(products[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mainImage = view.findViewById<ImageView>(R.id.mainImage)
        private val category = view.findViewById<TextView>(R.id.category)
        private val productName = view.findViewById<TextView>(R.id.productName)
        private val price = view.findViewById<TextView>(R.id.price)
        private val defaultPrice = view.findViewById<TextView>(R.id.defaultPrice)
        private val sale_tag = view.findViewById<TextView>(R.id.sale_tag)

        fun bindData(product: Product) {
            Glide.with(mainImage)
                .load(product.images)
                .into(mainImage)

            category.text = product.categories
            productName.text = product.name
            if (product.sales > 0) {
                defaultPrice.visibility = View.VISIBLE
                sale_tag.visibility = View.VISIBLE
                defaultPrice.text = HtmlCompat.fromHtml(
                    context.getString(R.string.default_price, product.prices),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                price.text = context.getString(R.string.price, product.sales)
            } else {
                defaultPrice.visibility = View.GONE
                sale_tag.visibility = View.GONE
                price.text = context.getString(R.string.price, product.prices)
            }
        }
    }
}