package com.example.arfashion.presentation.app.presentation.product.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Tag

class TagFilterAdapter : RecyclerView.Adapter<TagFilterAdapter.ViewHolder>() {

    private var list: MutableList<Tag> = mutableListOf()

    var checkBoxCheckEvent: ((tag: Tag, isCheck: Boolean) -> Unit)? = null

    fun setData(data: List<Tag>?) {
        this.list.clear()
        data?.let {
            this.list.addAll(data)
        }

        notifyDataSetChanged()
    }

    fun selectTag(id: String, isCheck: Boolean) {
        val tag = list.find { it.id == id }
        tag?.let {
            it.isSeltected = isCheck
            notifyItemChanged(list.indexOf(it))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter_tags, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tag = view.findViewById<CheckBox>(R.id.tagsCheckbox)

        init {
            tag.setOnCheckedChangeListener { _, isChecked ->
                checkBoxCheckEvent?.invoke(list[adapterPosition], isChecked)
            }
        }

        fun bindData(data: Tag) {
            tag.text = data.name
            tag.isChecked = data.isSeltected
        }
    }
}