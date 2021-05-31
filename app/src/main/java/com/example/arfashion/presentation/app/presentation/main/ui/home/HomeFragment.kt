package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.arfashion.R
import com.example.arfashion.presentation.app.*
import com.example.arfashion.presentation.app.presentation.cart.CartActivity
import com.example.arfashion.presentation.app.presentation.cart.CartViewModel
import com.example.arfashion.presentation.app.presentation.main.HomeToCategoriesShareViewModel
import com.example.arfashion.presentation.app.presentation.main.ui.categories.KEY_PRODUCT_ID
import com.example.arfashion.presentation.app.presentation.product.ProductAdapter
import com.example.arfashion.presentation.app.presentation.product.detail.ProductDetailActivity
import com.example.arfashion.presentation.app.widget.indicator.PagerIndicatorController
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Carousel
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_search_home.*
import java.util.*

class HomeFragment : Fragment() {
    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()

            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var bestSellerProductAdapter: ProductAdapter

    //TODO: recommendProductAdapter

    private lateinit var carouselAdapter: CarouselAdapter

    private val homeViewModel by viewModels<HomeViewModel> ()

    private val homeToCategoriesShareViewModel: HomeToCategoriesShareViewModel by activityViewModels()

    private val cartViewModel: CartViewModel by viewModels (ownerProducer = {this})

    private lateinit var bestSellerPagerIndicatorController : PagerIndicatorController

    private lateinit var carouselPagerIndicatorController: PagerIndicatorController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    init {
        lifecycleScope.launchWhenCreated {
            homeViewModel.getCarouselList()
            cartViewModel.getCart()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bestSellerProductAdapter = ProductAdapter(requireContext(), true)
        bestSellerPager.adapter = bestSellerProductAdapter
        bestSellerPagerIndicatorController = PagerIndicatorController(bestSellerPager, bestSellerSlide) {
            it.checkItemsAre<Product>()?.let { products ->
                bestSellerProductAdapter.setProducts(products)
            }
        }

        carouselAdapter = CarouselAdapter()
        carousel.adapter = carouselAdapter
        carouselPagerIndicatorController = PagerIndicatorController(carousel, carouselSlide) {
            it.checkItemsAre<Carousel>()?.let { carousels ->
                carouselAdapter.setData(carousels)
            }
        }

        //TODO: connect api, observer data here
        //TODO: Hard code, remove
        val products = listOf(
            Product(
                id = "605a279c928cf217986aad9d",
                name = "Áo 1",
                tag = listOf("T-Shirt"),
                images = listOf("https://product.hstatic.net/200000053174/product/4apkh006trt-295k_7cb61b16ced4411a81da614a5f544759_master.jpg"),
                prices = 300000,
                priceSale = /*Sales(100000, Date(2021, 12, 31))*/ 100000
            ),
            Product(
                id = "605a279c928cf217986aad9d",
                name = "Quần 1" ,
                tag = listOf("Pan"),
                images = listOf("https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg"),
                prices = 500000,
                priceSale = 500000
            ),
            Product(
                id = "605a279c928cf217986aad9d",
                name ="Quần 1" ,
                tag = listOf("Pan"),
                images = listOf("https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg"),
                prices = 500000,
                priceSale = 500000
            ),
            Product(
                id = "605a279c928cf217986aad9d",
                name ="Áo 2" ,
                tag = listOf("T-Shirt"),
                images = listOf("https://product.hstatic.net/200000053174/product/4apkh007ttt_-_295k_5db812b469f84e2aa34a51a79a460dad_master.jpg"),
                prices = 300000,
                priceSale = /*Sales(50000, Date(2021, 12, 31))*/ 50000
            ),
        )

        val temp = if (products.size > 4) {
            products.subList(0, 3)
        } else {
            products
        }
        //end TODO
        bestSellerPagerIndicatorController.handleData(temp)

        homeViewModel.carousel.observe(viewLifecycleOwner, {
            when (it) {
                is ARResult.Success -> {
                    handleCarouselData(it.data)
                }
                is ARResult.Error -> {}
            }
        })

        cartViewModel.cart.observe(viewLifecycleOwner, {
            when (it) {
                is ARResult.Success -> {
                    it.data.product.size.let { size ->
                        if (size > 0) {
                            cartCount.visible()
                            cartCount.text = size.toString()
                        } else {
                            cartCount.gone()
                        }
                    }
                }
                is ARResult.Error -> {
                    cartCount.gone()
                }
            }
        })
        cartViewModel.loading.observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = it
        })

        searchArea.setOnClickListener {
            homeToCategoriesShareViewModel.onSearchClick()
        }

        cart.setOnClickListener {
            val intent = Intent(this@HomeFragment.context, CartActivity::class.java)
            startActivity(intent)
        }

        refreshLayout.setOnRefreshListener {
            homeViewModel.getCarouselList()
            cartViewModel.getCart()
        }

        bestSellerProductAdapter.productClickLister = {
            this.requireContext().openProductDetailActivity(it)
        }
    }

    private fun handleCarouselData(carousels: List<Carousel>) {
        carouselPagerIndicatorController.handleData(carousels)
    }
}