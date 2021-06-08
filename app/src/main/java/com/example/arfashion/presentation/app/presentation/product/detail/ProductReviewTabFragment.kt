package com.example.arfashion.presentation.app.presentation.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.product.comment.CommentViewModel
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Comment
import kotlinx.android.synthetic.main.fragment_product_review_tab.*

class ProductReviewTabFragment : Fragment() {
    companion object {
        fun newInstance(): ProductReviewTabFragment {
            return ProductReviewTabFragment()
        }
    }

    private val commentViewModel: CommentViewModel by activityViewModels ()

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_review_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reviewAdapter = ReviewAdapter()
        with(reviewList) {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(context)
        }

        commentViewModel.comment.observe(viewLifecycleOwner) {
            when(it) {
                is ARResult.Success -> {
                    it.data.let { list ->
                        val temp = if (list.size >= 2) {
                            list.subList(0, 2)
                        } else {
                            list
                        }

                        handleData(temp)
                    }
                }
                is ARResult.Error -> {
                    Toast.makeText(this.requireContext(), it.throwable.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        commentViewModel.total.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> {
                    label.text = requireContext().getString(R.string.review_label, it.data)
                }
                is ARResult.Error -> {
                    label.text = requireContext().getString(R.string.review_label, 0)
                }
            }
        }
    }

    private fun handleData(comment: List<Comment>) {
        if (comment.isEmpty()) {
            showNoReviewAlert()
            viewAllBtn.gone()
            return
        }
        showReviewList()
        viewAllBtn.visible()
        reviewAdapter.setData(comment)
    }

    private fun showReviewList() {
        reviewList.visible()
        noReviewAlert.gone()
    }

    private fun showNoReviewAlert() {
        noReviewAlert.visible()
        reviewList.gone()
    }
}