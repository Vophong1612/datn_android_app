package com.example.arfashion.presentation.app.presentation.product.detail

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.presentation.app.dp

class RelatedProductItemOffset(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val verticalOffset = context.dp(10f)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition

        if (position != 0) {
            outRect.top = verticalOffset
        }

        if (position == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.bottom = verticalOffset
        }
    }
}