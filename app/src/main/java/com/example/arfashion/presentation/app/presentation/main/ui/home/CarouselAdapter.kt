package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.data.model.Carousel

class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.ViewHolder>() {

    private var carousels: MutableList<Carousel> = mutableListOf()

    fun setData(carousels: List<Carousel>?) {
        this.carousels.clear()
        carousels?.let {
            this.carousels.addAll(carousels)
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return carousels.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(carousels[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val mainImage = view.findViewById<ImageView>(R.id.carousel)

        fun bindData(carousel: Carousel) {
            Glide.with(mainImage)
                .load(carousel.image)
                .into(mainImage)
        }
    }
}