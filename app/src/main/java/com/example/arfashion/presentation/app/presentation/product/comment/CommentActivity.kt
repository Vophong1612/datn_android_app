package com.example.arfashion.presentation.app.presentation.product.comment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.detail.ReviewAdapter
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Comment
import kotlinx.android.synthetic.main.activity_comment.*
import kotlinx.android.synthetic.main.activity_comment.refreshLayout
import kotlinx.android.synthetic.main.layout_back_header.*

class CommentActivity : AppCompatActivity() {

    private val commentViewModel: CommentViewModel by viewModels()

    private lateinit var reviewAdapter: ReviewAdapter

    private var productId: String? = null

    private var comments: MutableList<Comment> = mutableListOf()

    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        productId = intent.extras?.getString(KEY_PRODUCT_ID)

        back_icon.setOnClickListener {
            finish()
        }
        screen_name.text = getString(R.string.product_review_tab)
        reviewAdapter = ReviewAdapter()
        with(commentList) {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lm = layoutManager as LinearLayoutManager

                    if (lm.findLastCompletelyVisibleItemPosition() == comments.size - 1) {
                        if (!refreshLayout.isRefreshing) {
                            getData(offset)
                        }
                    }
                }
            })
        }

        getData(offset)

        refreshLayout.setOnRefreshListener {
            offset = 0
            getData(offset)
        }

        commentViewModel.comment.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    handleData(it.data)
                }
                is ARResult.Error -> {
                    Toast.makeText(
                        this.applicationContext,
                        it.throwable.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        commentViewModel.total.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    label.text = this.applicationContext.getString(R.string.review_label, it.data)
                }
                is ARResult.Error -> {
                    label.text = this.applicationContext.getString(R.string.review_label, 0)
                }
            }
        }

        commentViewModel.offset.observe(this) {
            when (it) {
                is ARResult.Success -> offset = it.data
                is ARResult.Error -> {
                }
            }
        }

        commentViewModel.loading.observe(this) {
            refreshLayout.isRefreshing = it
        }
    }

    private fun getData(offset: Int) {
        productId?.let {
            commentViewModel.getComment(it, offset = offset)
        }
    }

    private fun handleData(comment: List<Comment>) {
        if (comment.isEmpty() && offset == 0) {
            showNoReviewAlert()
            return
        }
        comments.addAll(comment)
        if (offset != 0) {
            reviewAdapter.addData(comment)
        } else {
            reviewAdapter.setData(comment)
        }
        showReviewList()
    }

    private fun showReviewList() {
        commentList.visible()
        noReviewAlert.gone()
    }

    private fun showNoReviewAlert() {
        noReviewAlert.visible()
        commentList.gone()
    }
}