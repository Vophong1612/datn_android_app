package com.example.arfashion.presentation.app.presentation.product.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R

class ThumbnailAdapter : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

    private var thumbnails: MutableList<String> = mutableListOf()

    var thumbnailClickListener: ((Int) -> Unit)? = null

    var selectedIndex = 0

    private var internalSelectedListener = object : ((Int) -> Unit) {
        override fun invoke(index: Int) {
            val previousSelectedIndex = selectedIndex
            selectedIndex = index
            if (previousSelectedIndex in thumbnails.indices) {
                notifyItemChanged(previousSelectedIndex)
            }
            if (selectedIndex in thumbnails.indices) {
                notifyItemChanged(selectedIndex)
                thumbnailClickListener?.invoke(selectedIndex)
            }
        }
    }

    fun setData(thumbnail: List<String>?) {
        this.thumbnails.clear()
        thumbnail?.let {
            this.thumbnails.addAll(it)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_thumbnail_list, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return thumbnails.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(thumbnails[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val thumbnailImage = view.findViewById<ImageView>(R.id.thumbnailItem)

        init {
            itemView.setOnClickListener{
                internalSelectedListener(adapterPosition)
            }
        }

        fun bindData(thumbnail: String) {
            Glide.with(thumbnailImage)
                .load(thumbnail)
                .into(thumbnailImage)

            thumbnailImage.isSelected = selectedIndex == adapterPosition
        }
    }
}