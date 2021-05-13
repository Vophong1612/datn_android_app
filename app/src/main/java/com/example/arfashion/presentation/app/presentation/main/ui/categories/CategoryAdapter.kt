package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.model.Category

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var list: MutableList<Category> = mutableListOf()

    var itemClickListener : ((categoryId: String) -> Unit)? = null

    fun setData(data: List<Category>?) {
        this.list.clear()
        data?.let {
            this.list.addAll(data)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val categoryName = view.findViewById<TextView>(R.id.categoryName)
        private val image = view.findViewById<ImageView>(R.id.image)
        private val tagList = view.findViewById<RecyclerView>(R.id.tagList)
        private val tagEmptyAlert = view.findViewById<TextView>(R.id.tagEmptyAlert)

        private val tagAdapter = TagAdapter()

        init {
            with(tagList) {
                adapter = tagAdapter
                layoutManager = GridLayoutManager(context, 2)
            }

            image.setOnClickListener {
                itemClickListener?.invoke(list[adapterPosition].id)
            }
        }

        fun bindData(data: Category) {
            Glide.with(image)
                .load(data.imageCategory)
                .placeholder(R.drawable.img_default_category)
                .error(R.drawable.img_default_category)
                .into(image)

            categoryName.text = data.name

            if (data.tag.isEmpty()) {
                tagEmptyAlert.visible()
                tagList.gone()
                tagEmptyAlert.text = itemView.context.getString(R.string.tag_could_not_be_found)
                return
            }
            tagAdapter.setData(data.tag)
        }
    }
}