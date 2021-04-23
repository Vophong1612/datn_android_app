package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.example.arfashion.R
import com.example.arfashion.presentation.app.widget.indicator.IndicatorSlideView
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Carousel
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.services.ProductService
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {
    companion object {
//        const val TAG = "HomeFragment"

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

    private lateinit var homeViewModel: HomeViewModel

    private val productService = ProductService.create()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        homeViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(productService) as T
            }
        })[HomeViewModel::class.java]

        homeViewModel.getCarouselList()

        productAdapter = ProductAdapter(requireContext())
        recommendPager.adapter = productAdapter

        carouselAdapter = CarouselAdapter()
        carousel.adapter = carouselAdapter

        setUpIndicatorSlide(carousel, carouselSlide)

        setUpIndicatorSlide(recommendPager, recommendSlide)

        //TODO: Hard code, remove
        val products = listOf(
            Product(
                "Áo 1",
                "T-Shirt",
                images = "https://product.hstatic.net/200000053174/product/4apkh006trt-295k_7cb61b16ced4411a81da614a5f544759_master.jpg",
                prices = 300000,
                sales = 100000
            ),
            Product(
                "Quần 1" ,
                "Pan",
                images = "https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg",
                prices = 500000
            ),
            Product(
                "Quần 1" ,
                "Pan",
                images = "https://product.hstatic.net/200000053174/product/quan_au_nam_biluxury3_a1d8b22461134565b4a09cd90dc58666_master.jpg",
                prices = 500000
            ),
            Product(
                "Áo 2" ,
                "T-Shirt",
                images = "https://product.hstatic.net/200000053174/product/4apkh007ttt_-_295k_5db812b469f84e2aa34a51a79a460dad_master.jpg",
                prices = 300000,
                sales = 50000
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
            recommendSlide.visibility = View.VISIBLE
            recommendPager.adapter?.itemCount?.let { it -> recommendSlide.setDots(it) }
            recommendPager.setCurrentItem(1, true)
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