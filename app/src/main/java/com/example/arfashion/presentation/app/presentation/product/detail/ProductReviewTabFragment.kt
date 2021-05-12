package com.example.arfashion.presentation.app.presentation.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
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

    private val productDetailViewModel: ProductDetailViewModel by activityViewModels()

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

        productDetailViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> {
                    handleData(it.data.comments)
                }
                is ARResult.Error -> { }
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

        label.text = requireContext().getString(R.string.review_label, comment.size)
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