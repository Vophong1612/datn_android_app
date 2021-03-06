package com.example.arfashion.presentation.app.presentation.product

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.Utils.Companion.standardFormat

class ProductAdapter(private val context: Context, private val isViewPager: Boolean) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private var products: MutableList<Product> = mutableListOf()

    var productClickLister: ((productId: String) -> Unit)? = null

    fun setProducts(products: List<Product>?) {
        this.products.clear()
        products?.let {
            this.products.addAll(products)
        }

        notifyDataSetChanged()
    }

    fun addProducts(products: List<Product>) {
        val start = this.products.size
        val end = start + products.size

        this.products.addAll(products)

        notifyItemRangeInserted(start, end)
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
        private val saleTag = view.findViewById<TextView>(R.id.sale_tag)

        init {
            if (isViewPager) {
                view.layoutParams = ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            itemView.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }
                productClickLister?.invoke(products[adapterPosition].id)
            }
        }

        fun bindData(product: Product) {
            Glide.with(mainImage)
                .load(product.images[0])
                .into(mainImage)

            category.text = product.tag[0]
            productName.text = product.name
            if (product.isSale) {
                defaultPrice.visibility = View.VISIBLE
                saleTag.visibility = View.VISIBLE
                defaultPrice.text = HtmlCompat.fromHtml(
                    context.getString(R.string.default_price, product.prices.standardFormat()),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                price.text =
                    context.getString(R.string.price, product.priceSale.standardFormat())
            } else {
                defaultPrice.visibility = View.GONE
                saleTag.visibility = View.GONE
                price.text = context.getString(R.string.price, product.prices.standardFormat())
            }
        }
    }
}