package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.presentation.app.dp

class SearchItemOffset(
    context: Context
) : RecyclerView.ItemDecoration() {

    private val verticalOffset = context.dp(10f)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = verticalOffset
    }
}