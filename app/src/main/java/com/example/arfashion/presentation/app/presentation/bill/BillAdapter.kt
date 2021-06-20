package com.example.arfashion.presentation.app.presentation.bill

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Bill
import com.example.arfashion.presentation.data.model.Product
import com.google.gson.Gson

class BillAdapter(private val context: Context) : RecyclerView.Adapter<BillAdapter.ViewHolder>() {

    private val data = mutableListOf<Bill>()

    val itemViewClickListener: (() -> Unit)? = null

    fun setData(list: List<Bill>?) {
        data.clear()
        list?.let {
            data.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addProducts(bills: List<Bill>) {
        val start = this.data.size
        val end = start + bills.size

        this.data.addAll(bills)

        notifyItemRangeInserted(start, end)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bill_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageProduct = view.findViewById<ImageView>(R.id.item_image_product)
        private val name = view.findViewById<TextView>(R.id.item_name)
        private val size = view.findViewById<TextView>(R.id.item_size)
        private val color = view.findViewById<TextView>(R.id.item_color)
        private val price = view.findViewById<TextView>(R.id.item_price)
        private val total = view.findViewById<TextView>(R.id.item_totalProduct)
        private val totalPayment = view.findViewById<TextView>(R.id.totalPaymentValue)
        private val showMoreProduct = view.findViewById<TextView>(R.id.showMoreProduct)

        init {
            itemView.setOnClickListener {
                itemViewClickListener?.invoke()
            }

            showMoreProduct.setOnClickListener {
                val intent = Intent(context, DetailBillActivity::class.java)
                intent.putExtra("detailBill", Gson().toJson(data[adapterPosition]))
                context.startActivity(intent)
            }

        }

        @SuppressLint("SetTextI18n")
        fun bindData(data: Bill) {
            if (data.products.isNotEmpty()) {
                val firstProduct = data.products[0]
                name.text = firstProduct.name
                total.text = "x " + firstProduct.total
                price.text = context.getString(R.string.price, firstProduct.prices)
                if (firstProduct.images.isNotEmpty()) {
                    Glide.with(imageProduct)
                        .load(firstProduct.images[0])
                        .into(imageProduct)
                }
                if (firstProduct.sizes.isNotEmpty()) {
                    size.text = context.getString(R.string.cart_size, firstProduct.sizes[0].name)
                }
                if (firstProduct.colors.isNotEmpty()) {
                    color.text = context.getString(R.string.cart_color, firstProduct.colors[0])
                }
            }
            totalPayment.text = context.getString(R.string.price, data.totalCost)
        }
    }
}