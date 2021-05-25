package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.openProductDetailActivity
import com.example.arfashion.presentation.app.presentation.product.ProductAdapter
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.activity_product_by_category.*
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

    private val categoriesViewModel: CategoriesViewModel by viewModels()

    private lateinit var productResultAdapter: ProductAdapter

    private var keyWord: String? = ""

    private var typeResult: ProductTypeResult = ProductTypeResult.OTHER

    init {
        lifecycleScope.launchWhenCreated {
            intent.extras?.let {
                keyWord = it.getString(KEY_KEYWORD)
                typeResult = it.getSerializable(KEY_TYPE_RESULT) as ProductTypeResult
            }

            if (typeResult == ProductTypeResult.SEARCH) {
                searchBox.setText(keyWord)
            }

            getData()
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_by_category)

        initData()

        initView()

    }

    private fun initData() {
        productResultAdapter = ProductAdapter(applicationContext, false)
        with(productResultList) {
            adapter = productResultAdapter
            layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(SearchItemOffset(applicationContext))
        }

        refreshLayout.setOnRefreshListener {
            keyWord?.let {
                categoriesViewModel.getProductListByCategory(it)
            }
        }

        categoriesViewModel.listProductByCategory.observe(this, {
            handleData(it.data)
        })
        categoriesViewModel.search.observe(this, {
            handleData(it.data)
        })
        categoriesViewModel.loading.observe(this) {
            refreshLayout.isRefreshing = it
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
            }.debounce(300)
                .collect {
                    if (it.isNotEmpty()) {
                        clearIcon.visible()
                        categoriesViewModel.searchByKeyWord(it)
                    } else {
                        clearSearchResult()
                    }
                }
        }

        clearIcon.setOnClickListener {
            clearSearchResult()
        }
    }

    private fun getData() {
        keyWord?.let {
            when (typeResult) {
                ProductTypeResult.SEARCH -> {
                    categoriesViewModel.searchByKeyWord(it)
                }
                ProductTypeResult.CATEGORY -> {
                    categoriesViewModel.getProductListByCategory(it)
                }
                else -> {
                }
            }
        }
    }

    private fun handleData(data: List<Product>?) {
        if (data.isNullOrEmpty()) {
            showAlert(getString(R.string.category_could_not_be_found))
            return
        }
        productResultAdapter.setProducts(data)
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