package com.example.arfashion.presentation.app.presentation.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.data.model.Product
import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker.doOnProgressChanged
import it.sephiroth.android.library.numberpicker.doOnStopTrackingTouch

class CartProductAdapter : RecyclerView.Adapter<CartProductAdapter.ViewHolder>() {

    private var data: MutableList<Product> = mutableListOf()

    var selectCbClickEvent: ((product: Product, isCheck: Boolean) -> Unit)? = null

    var productCountClickEvent: ((product: Product, value: Int) -> Unit)? = null

    var deleteProductClickEvent: ((product: Product, position: Int) -> Unit)? = null

    fun setData(products: List<Product>?) {
        data.clear()
        products?.let {
            data.addAll(it)
        }

        notifyDataSetChanged()
    }

    fun selectAll(isSelect: Boolean) {
        data.forEach { product ->
            product.isCartCheck = isSelect
        }

        notifyDataSetChanged()
    }

    fun deleteProduct(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart_list, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val selectCb = view.findViewById<CheckBox>(R.id.selectCb)
        private val image = view.findViewById<ImageView>(R.id.image)
        private val name = view.findViewById<TextView>(R.id.name)
        private val size = view.findViewById<TextView>(R.id.size)
        private val color = view.findViewById<TextView>(R.id.color)
        private val productCount = view.findViewById<NumberPicker>(R.id.productCount)
        private val price = view.findViewById<TextView>(R.id.productPrice)
        private val defaultPrice = view.findViewById<TextView>(R.id.defaultPrice)
        private val deleteBtn = view.findViewById<ImageView>(R.id.deleteProductCartBtn)

        private var beforeProgress = 0

        init {
            selectCb.setOnCheckedChangeListener { _, isChecked ->
                selectCbClickEvent?.invoke(data[adapterPosition], isChecked)

                productCount.isEnabled = !isChecked
            }
            productCount.doOnProgressChanged { numberPicker , progress, _ ->
                if (numberPicker.isFocused) {
                    productCountClickEvent?.invoke(data[adapterPosition], progress - beforeProgress)
                }
            }
            deleteBtn.setOnClickListener {
                deleteProductClickEvent?.invoke(data[adapterPosition], adapterPosition)
            }
        }

        fun bindData(data: Product) {
            Glide.with(image)
                .load(data.images[0])
                .placeholder(R.drawable.img_default_category)
                .error(R.drawable.img_default_category)
                .into(image)

            name.text = data.name

            productCount.progress = data.total
            beforeProgress = data.total

            data.sizes.let {
                if (it.isNotEmpty()) {
                    size.text = size.context.getString(R.string.cart_size, data.sizes[0].name)
                }
            }
            data.colors.let {
                if (it.isNotEmpty()) {
                    color.text = color.context.getString(R.string.cart_color, data.colors[0])
                }
            }

            price.text = price.context.getString(R.string.price, data.priceSale)
            if (data.isSale) {
                defaultPrice.text = HtmlCompat.fromHtml(
                    defaultPrice.context.getString(R.string.default_price, data.prices),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            } else {
                defaultPrice.gone()
            }

            selectCb.isChecked = data.isCartCheck
        }
    }
}