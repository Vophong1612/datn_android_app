package com.example.arfashion.presentation.app.presentation.product.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.model.Comment
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var comments: MutableList<Comment> = mutableListOf()

    fun setData(data: List<Comment>?) {
        comments.clear()
        data?.let {
            comments.addAll(it)
        }

        notifyDataSetChanged()
    }

    fun addData(data: List<Comment>) {
        val start = comments.size
        val end = start + data.size

        comments.addAll(data)

        notifyItemRangeInserted(start, end)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment_list, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(comments[position])
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avatar = view.findViewById<ImageView>(R.id.avatarView)
        private val rating = view.findViewById<RatingBar>(R.id.rating)
        private val name = view.findViewById<TextView>(R.id.name)
        private val dateComment = view.findViewById<TextView>(R.id.dateComment)
        private val title = view.findViewById<TextView>(R.id.title)
        private val comment = view.findViewById<TextView>(R.id.comment)
        private val imagesAttach = view.findViewById<TextView>(R.id.imageAttach)

        fun bindData(data: Comment) {
            Glide.with(avatar)
                .load(data.owner.avatar)
                .placeholder(R.drawable.bg_blank_avatar)
                .error(R.drawable.bg_blank_avatar)
                .into(avatar)

            rating.rating = data.stars
            name.text = data.owner.name
            comment.text = data.content
            title.text = data.title
            data.images.size.let {
                if (it == 0) {
                    imagesAttach.gone()
                } else {
                    imagesAttach.visible()
                    imagesAttach.text = imagesAttach.context.getString(R.string.attach_image_review, data.images.size)
                }
            }

            val parser = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
            dateComment.text = parser.format(data.createAt)
        }
    }
}