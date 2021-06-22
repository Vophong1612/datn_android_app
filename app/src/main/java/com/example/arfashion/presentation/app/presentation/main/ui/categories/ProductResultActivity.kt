package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.openProductDetailActivity
import com.example.arfashion.presentation.app.presentation.product.ProductAdapter
import com.example.arfashion.presentation.app.presentation.product.filter.FilterController
import com.example.arfashion.presentation.app.presentation.product.filter.FilterDialog
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.activity_product_by_category.*
import kotlinx.android.synthetic.main.activity_product_by_category.refreshLayout
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_back_header.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

const val KEY_KEYWORD = "key_bundle_keyword"
const val KEY_TYPE_RESULT = "key_bundle_type_result"

enum class ProductTypeResult {
    SEARCH,
    CATEGORY,
    OTHER
}

class ProductResultActivity : AppCompatActivity() {

    private lateinit var categoriesViewModel: CategoriesViewModel

    private lateinit var productResultAdapter: ProductAdapter

    private var keyWord: String? = ""

    private var typeResult: ProductTypeResult = ProductTypeResult.OTHER

    private var productList: MutableList<Product> = mutableListOf()

    private var offset = 0

    private lateinit var filterController: FilterController

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_by_category)

        categoriesViewModel = ViewModelProvider(
            this@ProductResultActivity,
            MyViewModelFactory(applicationContext)
        ).get(CategoriesViewModel::class.java)

        getDataFromIntent()

        initData()

        initView()

    }

    private fun getDataFromIntent() {
        intent.extras?.let {
            keyWord = it.getString(KEY_KEYWORD)
            typeResult = it.getSerializable(KEY_TYPE_RESULT) as ProductTypeResult
        }

        if (typeResult == ProductTypeResult.SEARCH) {
            searchBox.setText(keyWord)
        }
    }

    private fun initData() {
        filterController = FilterController(applicationContext)
        productResultAdapter = ProductAdapter(applicationContext, false)
        with(productResultList) {
            adapter = productResultAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SearchItemOffset(applicationContext))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val lm = layoutManager as LinearLayoutManager

                    if (lm.findLastCompletelyVisibleItemPosition() == productList.size - 1) {
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
        categoriesViewModel.loading.observe(this) {
            refreshLayout.isRefreshing = it
        }
        categoriesViewModel.filter.observe(this) {
            when (it) {
                is ARResult.Success -> handleData(it.data)
                is ARResult.Error -> {
                }
            }
        }
        categoriesViewModel.offset.observe(this) {
            when (it) {
                is ARResult.Success -> offset = it.data
                is ARResult.Error -> {
                }
            }
        }
        categoriesViewModel.action.observe(this) {
            when (it) {
                is ARResult.Success -> getData(offset)
                is ARResult.Error -> {
                }
            }
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun initView() {
        back_icon.setOnClickListener {
            finish()
        }

        productResultAdapter.productClickLister = {
            openProductDetailActivity(it)
        }

        lifecycleScope.launch {
            callbackFlow {
                searchBox.doAfterTextChanged {
                    offer(it.toString())
                }
                awaitClose { cancel() }
            }.debounce(500)
                .collect {
                    if (it.isNotEmpty()) {
                        clearIcon.visible()
                        searchProduct(it, 0)
                    } else {
                        clearSearchResult()
                    }
                }
        }

        clearIcon.setOnClickListener {
            clearSearchResult()
            finish()
        }

        filterIcon.setOnClickListener {
            val dialog = FilterDialog.newInstance()
            if (supportFragmentManager.isStateSaved || dialog.isAdded) {
                return@setOnClickListener
            }
            dialog.show(supportFragmentManager, null)
        }
    }

    private fun getData(offset: Int) {
        keyWord?.let {
            when (typeResult) {
                ProductTypeResult.SEARCH -> {
                    searchProduct(it, offset)
                }
                ProductTypeResult.CATEGORY -> {
                    getProductByCategory(it, offset)
                }
                else -> {
                }
            }
        }
    }

    private fun searchProduct(keyword: String, offset: Int) {
        val sortPrice = filterController.getSortPrice() ?: ""
        val range = filterController.getPriceRangeBellow().toInt()
            .toString() + "," + filterController.getPriceRangeAbove().toInt().toString()
        val size = filterController.getSize() ?: ""
        var slTag = ""
        filterController.getTagsList().forEach {
            slTag += it.name + ","
        }
        categoriesViewModel.filterProduct(
            keyword = keyword,
            priceSort = sortPrice,
            size = size,
            priceRange = range,
            tags = slTag,
            offset = offset
        )
    }

    private fun getProductByCategory(catId: String, offset: Int) {
        val sortPrice = filterController.getSortPrice() ?: ""
        val range = filterController.getPriceRangeBellow().toInt()
            .toString() + "," + filterController.getPriceRangeAbove().toInt().toString()
        val size = filterController.getSize() ?: ""
        var slTag = ""
        filterController.getTagsList().forEach {
            slTag += it.name + ","
        }
        categoriesViewModel.filterProduct(
            categoryId = catId,
            priceSort = sortPrice,
            size = size,
            priceRange = range,
            tags = slTag,
            offset = offset
        )
    }

    private fun handleData(data: List<Product>) {
        if (data.isEmpty() && offset == 0) {
            showAlert(getString(R.string.product_could_not_be_found))
            return
        }

        if (offset != 0) {
            productResultAdapter.addProducts(data)
        } else {
            productResultAdapter.setProducts(data)
            productList.clear()
        }
        productList.addAll(data)
        showDataList()
    }

    private fun showAlert(message: String) {
        listEmptyAlert.visible()
        listEmptyAlert.text = message
        productResultList.gone()
    }

    private fun showDataList() {
        listEmptyAlert.gone()
        productResultList.visible()
    }

    private fun clearSearchResult() {
        clearIcon.gone()
        if (productResultAdapter.itemCount == 0) {
            showAlert(getString(R.string.category_could_not_be_found))
            return
        }
        searchBox.text.clear()
        productResultList.gone()
    }
}