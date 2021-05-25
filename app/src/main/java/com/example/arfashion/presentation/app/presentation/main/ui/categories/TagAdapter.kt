package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R

class TagAdapter : RecyclerView.Adapter<TagAdapter.ViewHolder>() {

    private var list: MutableList<String> = mutableListOf()

    fun setData(data: List<String>?) {
        this.list.clear()
        data?.let {
            this.list.addAll(data)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tag = view.findViewById<TextView>(R.id.tag)

        fun bindData(data: String) {
            tag.text = data
        }
    }
}