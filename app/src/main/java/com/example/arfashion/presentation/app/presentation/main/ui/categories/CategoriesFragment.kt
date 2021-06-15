package com.example.arfashion.presentation.app.presentation.main.ui.categories

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.visible
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Category
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_categories.refreshLayout
import kotlinx.android.synthetic.main.layout_search_categories.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val KEY_PRODUCT_ID = "key_bundle_product_id"

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

        categoryViewModel.loading.observe(viewLifecycleOwner, {
            refreshLayout.isRefreshing = it
        })

        categoryViewModel.category.observe(viewLifecycleOwner, {
            handleCategoriesData(it.data)
        })

        categoryAdapter.itemClickListener = { id ->
            val intent = Intent(this.requireContext(), ProductResultActivity::class.java)
            intent.putExtra(KEY_KEYWORD, id)
            intent.putExtra(KEY_TYPE_RESULT, ProductTypeResult.CATEGORY)
            startActivity(intent)
        }

        refreshLayout.setOnRefreshListener {
                categoryViewModel.getCategories()
        }

        lifecycleScope.launch {
            callbackFlow {
                searchBox.doAfterTextChanged {
                    offer(it.toString())
                }
                awaitClose { cancel() }
            }.debounce(700)
                .collect {
                    if (it.isNotEmpty()){
                        val intent = Intent(this@CategoriesFragment.requireContext(), ProductResultActivity::class.java)
                        intent.putExtra(KEY_KEYWORD, it)
                        intent.putExtra(KEY_TYPE_RESULT, ProductTypeResult.SEARCH)
                        startActivity(intent)
                    }
                }
        }

        clearButton.setOnClickListener {
            searchBox.text.clear()
        }
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
    }

    private fun showCategoriesList() {
        listEmptyAlert.gone()
        categoryList.visible()
    }

    override fun onResume() {
        super.onResume()

        searchBox.text.clear()
    }
}