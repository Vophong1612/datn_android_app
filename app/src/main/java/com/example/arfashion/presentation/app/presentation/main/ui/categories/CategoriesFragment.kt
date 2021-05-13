package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.home.ProductAdapter
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Category
import com.example.arfashion.presentation.data.model.Product
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.refreshLayout
import kotlinx.android.synthetic.main.layout_search_categories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collect

class CategoriesFragment : Fragment() {
    companion object {
        const val TAG = "DashboardFragment"
        fun newInstance(): CategoriesFragment {
            return CategoriesFragment()
        }
    }

    private val categoryAdapter by lazy {
        CategoryAdapter()
    }

    private val searchAdapter by lazy {
        ProductAdapter(requireContext(), false)
    }

    private lateinit var sortSpinnerAdapter : ArrayAdapter<String>

    private val categoryViewModel by viewModels<CategoriesViewModel>()

    init {
        lifecycleScope.launchWhenCreated {
            categoryViewModel.getCategories()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(categoryList) {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(CategoryItemOffset(context))
        }

        with(searchList) {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(SearchItemOffset(context))
        }

        val listSortOptions = mutableListOf("Price Up", "Price Down")
        sortSpinnerAdapter = ArrayAdapter(requireContext(), R.layout.item_sort_spinner, R.id.sortOption, listSortOptions)
        sortSpinner.adapter = sortSpinnerAdapter

        categoryViewModel.loading.observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = it
        })

        categoryViewModel.category.observe(viewLifecycleOwner, {
            handleCategoriesData(it.data)
        })

        categoryViewModel.search.observe(viewLifecycleOwner, {
            handleSearchData(it.data)
        })

        categoryAdapter.itemClickListener = {
            categoryViewModel.getProductListByCategory(it)
        }

        categoryViewModel.listProductByCategory.observe(viewLifecycleOwner, {
            handleSearchData(it.data)
        })

        refreshLayout.setOnRefreshListener {
            val searchContent = searchBox.text.toString()
            if (searchContent.isNotEmpty()) {
                categoryViewModel.searchByKeyWord(searchContent)
            } else {
                categoryViewModel.getCategories()
            }
        }

        lifecycleScope.launch {
            callbackFlow {
                searchBox.doAfterTextChanged {
                    offer(it.toString())
                }
                awaitClose { cancel() }
            }.debounce(300)
                .collect {
                    if (it.isNotEmpty()){
                        clearIcon.visible()
                        categoryViewModel.searchByKeyWord(it)
                    } else {
                        clearSearchResult()
                    }
                }
        }

        clearIcon.setOnClickListener {
            searchBox.text.clear()
        }

        sortSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun handleSearchData(data: List<Product>?) {
        if (data.isNullOrEmpty()) {
            showAlert(requireContext().getString(R.string.product_could_not_be_found))
            return
        }
        searchAdapter.setProducts(data)
        showSearchList()
    }

    private fun handleCategoriesData(data: List<Category>?) {
        if (data.isNullOrEmpty()) {
            showAlert(requireContext().getString(R.string.category_could_not_be_found))
            return
        }
        categoryAdapter.setData(data)
        showCategoriesList()
    }

    private fun showAlert(message: String){
        listEmptyAlert.visible()
        listEmptyAlert.text = message
        categoryList.gone()
        searchGroup.gone()
    }

    private fun showCategoriesList() {
        listEmptyAlert.gone()
        categoryList.visible()
        searchGroup.gone()
    }

    private fun showSearchList() {
        listEmptyAlert.gone()
        categoryList.gone()
        searchGroup.visible()
    }

    private fun clearSearchResult() {
        clearIcon.gone()
        if (categoryAdapter.itemCount == 0) {
            showAlert(requireContext().getString(R.string.category_could_not_be_found))
            return
        }
        showCategoriesList()
    }
}