package com.example.arfashion.presentation.app.presentation.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.payment.ProductInBill
import com.example.arfashion.presentation.services.Utils.Companion.standardFormat
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentProductsAdapter(private val context: Activity) :
    RecyclerView.Adapter<PaymentProductsAdapter.ViewHolder>() {

    private var productList: MutableList<ProductInBill> = mutableListOf()

    fun setProductList(productList: List<ProductInBill>?) {
        this.productList.clear()
        productList?.let {
            this.productList.addAll(productList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_payment, parent, false)
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

        init {
            itemView.setOnClickListener {

            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(res: ProductInBill) {
            name.text = res.name
            color.text = context.getString(R.string.color_lbl) + res.color
            size.text = context.getString(R.string.size_lbl) + res.size
            totalProduct.text = "x " + res.total.toString()
            price.text = price.context.getString(R.string.price, res.price.standardFormat())
            Glide.with(image)
                .load(res.image)
                .into(image)
        }
    }

}