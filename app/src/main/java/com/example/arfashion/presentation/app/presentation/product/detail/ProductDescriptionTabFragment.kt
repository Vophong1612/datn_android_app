package com.example.arfashion.presentation.app.presentation.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.arfashion.R
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.fragment_product_desciption_tab.*

class ProductDescriptionTabFragment : Fragment() {
    companion object {
        fun newInstance(): ProductDescriptionTabFragment {
            return ProductDescriptionTabFragment()
        }
    }

    private val productDetailViewModel: ProductDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_product_desciption_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        productDetailViewModel.product.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> {
                    handleData(it.data)
                }
                is ARResult.Error -> { }
            }
        }
    }

    private fun handleData(product: Product?) {
        var description = "Not available"
        product?.let {
            description = ""
            description += product.details
            description += "<p><b>- Description: </b>" + product.description + "<p/>"
        }

        productDesciption.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}