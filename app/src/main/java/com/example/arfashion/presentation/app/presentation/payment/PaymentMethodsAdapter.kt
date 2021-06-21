package com.example.arfashion.presentation.app.presentation.payment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.models.payment.PaymentItem

class PaymentMethodsAdapter : RecyclerView.Adapter<PaymentMethodsAdapter.ViewHolder>() {

    private var methodList: MutableList<PaymentItem> = mutableListOf()

    var selectCbClickEvent: ((product: PaymentItem, isCheck: Boolean) -> Unit)? = null

    fun setMethodList(methodList: List<PaymentItem>?) {

        this.methodList.clear()

        methodList?.let {
            this.methodList.addAll(methodList)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_method, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return methodList.size
    }


    fun getCheckedItem(): String{
        methodList.forEach { item ->
            if (item.isChosen) {
                return item.name
            }
        }
        return ""
    }

    fun getCheckedItemId(): String{
        methodList.forEach { item ->
            if (item.isChosen) {
                return item._id
            }
        }
        return ""
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(methodList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val chosen = view.findViewById<RadioButton>(R.id.item_rb)

        init {
            chosen.setOnCheckedChangeListener { _, isChecked ->
                selectCbClickEvent?.invoke(methodList[adapterPosition], isChecked)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bindData(res: PaymentItem) {
            chosen.text = res.name
            chosen.isChecked = res.isChosen
        }
    }

}