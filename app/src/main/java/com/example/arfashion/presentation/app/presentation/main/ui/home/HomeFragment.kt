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
import androidx.viewpager2.widget.ViewPager2
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.cart.CartActivity
import com.example.arfashion.presentation.app.presentation.cart.CartViewModel
import com.example.arfashion.presentation.app.presentation.main.HomeToCategoriesShareViewModel
import com.example.arfashion.presentation.app.presentation.product.detail.ProductDetailActivity
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.app.widget.indicator.IndicatorSlideView
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

    private lateinit var productAdapter: ProductAdapter

    //TODO: recommendProductAdapter

    private lateinit var carouselAdapter: CarouselAdapter

    private val homeViewModel by viewModels<HomeViewModel> ()

    private val homeToCategoriesShareViewModel: HomeToCategoriesShareViewModel by activityViewModels()

    private val cartViewModel: CartViewModel by viewModels (ownerProducer = {this})

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
        productAdapter = ProductAdapter(requireContext(), true)
        bestSellerPager.adapter = productAdapter
        productAdapter.productClickLister = {
            val intent = Intent(this@HomeFragment.context, ProductDetailActivity::class.java)
            startActivity(intent)
        }

        carouselAdapter = CarouselAdapter()
        carousel.adapter = carouselAdapter

        setUpIndicatorSlide(carousel, carouselSlide)

        setUpIndicatorSlide(bestSellerPager, bestSellerSlide)

        //TODO: Hard code, remove
        val products = listOf(
            Product(
                name = "Áo 1",
                tag = listOf("T-Shirt"),
                images = listOf("https://product.hstatic.net/200000053174/product/4apkh006trt-295k_7cb61b16ced4411a81da614a5f544759_master.jpg"),
                prices = 300000,
                priceSale = /*Sales(100000, Date(2021, 12, 31))*/ 100000
            ),
            Product(
                name = "Quần 1" ,
                tag = listOf("Pan"),
                images = listOf("https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg"),
                prices = 500000
            ),
            Product(
                name ="Quần 1" ,
                tag = listOf("Pan"),
                images = listOf("https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg"),
                prices = 500000
            ),
            Product(
                name ="Áo 2" ,
                tag = listOf("T-Shirt"),
                images = listOf("https://product.hstatic.net/200000053174/product/4apkh007ttt_-_295k_5db812b469f84e2aa34a51a79a460dad_master.jpg"),
                prices = 300000,
                priceSale = /*Sales(50000, Date(2021, 12, 31))*/ 50000
            ),
        )

        //TODO: connect api, observer data here
        val temp = if (products.size > 4) {
            products.subList(0, 3)
        } else {
            products
        }
        val transformData = arrayListOf(temp.last())
        transformData.addAll(temp)
        transformData.add(temp.first())
        productAdapter.setProducts(transformData)
        if (products.size >= 2) {
            bestSellerSlide.visibility = View.VISIBLE
            bestSellerPager.adapter?.itemCount?.let { it -> bestSellerSlide.setDots(it) }
            bestSellerPager.setCurrentItem(1, true)
        } else {
            bestSellerSlide.visibility = View.GONE
        }
        //end TODO

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
    }

    private fun handleCarouselData(carousels: List<Carousel>) {
        val temp = if (carousels.size > 4) {
            carousels.subList(0, 3)
        } else {
            carousels
        }
        val transformData = arrayListOf(temp.last())
        transformData.addAll(temp)
        transformData.add(temp.first())

        carouselAdapter.setData(transformData)

        if (carousels.size >= 2) {
            carouselSlide.visibility = View.VISIBLE
            carousel.adapter?.itemCount?.let { count -> carouselSlide.setDots(count) }
            carousel.setCurrentItem(1, true)
        } else {
            carouselSlide.visibility = View.GONE
        }
    }

    private fun setUpIndicatorSlide(pager: ViewPager2, indicator: IndicatorSlideView) {
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.onDotSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                val currentPosition = indicator.getCurrentPosition()
                val dotsCount = indicator.getDotsCount()
                if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    if (currentPosition == 0)
                        pager.setCurrentItem(dotsCount - 2, false)
                    else if (currentPosition == dotsCount - 1)
                        pager.setCurrentItem(1, false)
                }
            }
        })
    }

}