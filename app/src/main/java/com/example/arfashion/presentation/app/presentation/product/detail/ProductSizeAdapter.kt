package com.example.arfashion.presentation.app.presentation.product.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Size

class ProductSizeAdapter : RecyclerView.Adapter<ProductSizeAdapter.ViewHolder>() {

    private var sizesList: MutableList<Size> = mutableListOf()

    var sizeClickListener: ((Int) -> Unit)? = null

    var selectedIndex = -1

    private var internalSelectedListener = object : ((Int) -> Unit) {
        override fun invoke(index: Int) {
            val previousSelectedIndex = selectedIndex
            selectedIndex = index
            if (previousSelectedIndex in sizesList.indices) {
                notifyItemChanged(previousSelectedIndex)
            }
            if (selectedIndex in sizesList.indices) {
                notifyItemChanged(selectedIndex)
                sizeClickListener?.invoke(selectedIndex)
            }
        }
    }

    fun setData(size: List<Size>?) {
        this.sizesList.clear()
        size?.let {
            this.sizesList.addAll(it)
        }

        notifyDataSetChanged()
    }

    fun selectedIndex(size: String) {
        val index = sizesList.indexOfFirst { size == it.name }
        val beforeIndex = selectedIndex
        selectedIndex = index
        notifyItemChanged(beforeIndex)
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_size_list, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return sizesList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(sizesList[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val sizeText = view.findViewById<TextView>(R.id.size)

        init {
            itemView.setOnClickListener{
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                internalSelectedListener(adapterPosition)
            }
        }

        fun bindData(size: Size) {
            sizeText.text = size.name
            sizeText.isSelected = selectedIndex == adapterPosition
        }
    }
}