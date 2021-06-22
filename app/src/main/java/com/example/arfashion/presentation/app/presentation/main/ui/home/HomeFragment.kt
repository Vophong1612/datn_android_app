package com.example.arfashion.presentation.app.presentation.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.arfashion.R
import com.example.arfashion.presentation.app.*
import com.example.arfashion.presentation.app.presentation.cart.CartActivity
import com.example.arfashion.presentation.app.presentation.cart.CartViewModel
import com.example.arfashion.presentation.app.presentation.main.HomeToCategoriesShareViewModel
import com.example.arfashion.presentation.app.presentation.product.ProductAdapter
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

    private lateinit var newestProductAdapter: ProductAdapter

    private lateinit var carouselAdapter: CarouselAdapter

    private lateinit var homeViewModel: HomeViewModel

    private val homeToCategoriesShareViewModel: HomeToCategoriesShareViewModel by activityViewModels()

    private lateinit var cartViewModel: CartViewModel

    private lateinit var bestSellerPagerIndicatorController: PagerIndicatorController

    private lateinit var newestProductPagerIndicatorController: PagerIndicatorController

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
            homeViewModel =
                ViewModelProvider(this@HomeFragment, MyViewModelFactory(requireContext())).get(
                    HomeViewModel::class.java
                )
            cartViewModel =
                ViewModelProvider(this@HomeFragment, MyViewModelFactory(requireContext())).get(
                    CartViewModel::class.java
                )

            homeViewModel.getCarouselList()
            homeViewModel.getBestSellerProduct()
            homeViewModel.getNewestProduct()
            cartViewModel.getCart()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bestSellerProductAdapter = ProductAdapter(requireContext(), true)
        bestSellerPager.adapter = bestSellerProductAdapter
        bestSellerPagerIndicatorController =
            PagerIndicatorController(bestSellerPager, bestSellerSlide) {
                it.checkItemsAre<Product>()?.let { products ->
                    bestSellerProductAdapter.setProducts(products)
                }
            }

        newestProductAdapter = ProductAdapter(requireContext(), true)
        newestPager.adapter = newestProductAdapter
        newestProductPagerIndicatorController = PagerIndicatorController(newestPager, newestSlide) {
            it.checkItemsAre<Product>()?.let { products ->
                newestProductAdapter.setProducts(products)
            }
        }

        carouselAdapter = CarouselAdapter()
        carousel.adapter = carouselAdapter
        carouselPagerIndicatorController = PagerIndicatorController(carousel, carouselSlide) {
            it.checkItemsAre<Carousel>()?.let { carousels ->
                carouselAdapter.setData(carousels)
            }
        }

        homeViewModel.bestSellerProduct.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> handleBestProductData(it.data)
                is ARResult.Error -> {
                    bestSellerGroup.gone()
                }
            }
        }

        homeViewModel.newestProduct.observe(viewLifecycleOwner) {
            when (it) {
                is ARResult.Success -> handleNewestProductData(it.data)
                is ARResult.Error -> {
                    newestGroup.gone()
                }
            }
        }

        homeViewModel.carousel.observe(viewLifecycleOwner, {
            when (it) {
                is ARResult.Success -> {
                    handleCarouselData(it.data)
                }
                is ARResult.Error -> {
                }
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

        newestProductAdapter.productClickLister = {
            this.requireContext().openProductDetailActivity(it)
        }
    }

    private fun handleNewestProductData(data: List<Product>) {
        if (data.isEmpty()) {
            bestSellerGroup.gone()
            return
        }

        newestGroup.visible()
        newestProductPagerIndicatorController.handleData(data)
    }

    private fun handleBestProductData(data: List<Product>) {
        if (data.isEmpty()) {
            bestSellerGroup.gone()
            return
        }

        bestSellerGroup.visible()
        val temp = if (data.size > 4) {
            data.subList(0, 3)
        } else {
            data
        }
        bestSellerPagerIndicatorController.handleData(temp)
    }

    private fun handleCarouselData(carousels: List<Carousel>) {
        carouselPagerIndicatorController.handleData(carousels)
    }
}