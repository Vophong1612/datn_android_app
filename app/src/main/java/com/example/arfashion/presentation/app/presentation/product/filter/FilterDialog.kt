package com.example.arfashion.presentation.app.presentation.product.filter

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.categories.CategoriesViewModel
import com.example.arfashion.presentation.app.presentation.product.detail.ProductSizeAdapter
import com.example.arfashion.presentation.app.presentation.product.detail.ThumbnailItemOffset
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Category
import com.example.arfashion.presentation.data.model.Size
import com.example.arfashion.presentation.data.model.Tag
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.dialog_filter.*

private const val KEY_SHARED_PREFERENCES = "key_shared_preferences_filter_state"
private const val KEY_CATEGORY_SELECTED = "key_category_selected"
private const val KEY_SORT_PRICE_SELECTED = "key_sort_price_selected"
private const val KEY_SIZE_SELECTED = "key_sized_selected"
private const val KEY_PRICE_RANGE_BELOW = "key_price_range_below"
private const val KEY_PRICE_RANGE_UP = "key_price_range_up"
private const val KEY_TAGS_SELECTED = "key_tags_selected"

class FilterDialog : DialogFragment() {
    companion object {
        fun newInstance(): FilterDialog {
            return FilterDialog()
        }
    }

    private val categoriesViewModel by activityViewModels<CategoriesViewModel>()

    private lateinit var productSizeAdapter: ProductSizeAdapter

    private lateinit var tagAdapter: TagFilterAdapter

    private var category: List<Category>? = listOf()

    private var tag: List<Tag>? = listOf()

    private var size: List<Size>? = listOf()

    private var selectedSizeId: String = ""

    private var sortPrice = "asc"

    private var catId: String = "all"

    private var selectedTag: MutableList<Tag> = mutableListOf()

    private val pref by lazy {
        requireContext().getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private val gson = GsonBuilder().create()

    init {
        lifecycleScope.launchWhenCreated {
            categoriesViewModel.getCategories()
            categoriesViewModel.getSize()
            categoriesViewModel.getTag()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_filter, container, false)
    }

    @Suppress("DEPRECATION")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.let {
            val dm = Resources.getSystem().displayMetrics
            val width = (dm.widthPixels * 0.88f).toInt()
            val height = (width * 1.5f).toInt()
            it.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productSizeAdapter = ProductSizeAdapter()
        with(sizeList) {
            adapter = productSizeAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(ThumbnailItemOffset(context))
        }

        tagAdapter = TagFilterAdapter()
        with(tagsList) {
            adapter = tagAdapter
            layoutManager = GridLayoutManager(this.context, 2)
        }
        tagAdapter.checkBoxCheckEvent = { tag, isCheck ->
            if (isCheck) {
                if (selectedTag.indexOf(tag) == -1) {
                    selectedTag.add(tag)
                }
            } else {
                if (selectedTag.indexOf(tag) != -1) {
                    selectedTag.remove(tag)
                }
            }
        }

        setUpView()

        clearFilter.setOnClickListener {
            dismiss()
            categoriesViewModel.filterProduct()
            pref.edit().clear().apply()
        }

        applyBtn.setOnClickListener {
            dismiss()

            //category
            val button = view.findViewById<RadioButton>(categoryList.checkedRadioButtonId)
            button?.let {
                when (val catName = button.text) {
                    "all" -> {
                    }
                    else -> {
                        category?.let {
                            it.forEach { cat ->
                                if (cat.name == catName) {
                                    catId = cat.id
                                }
                            }
                        }
                    }
                }
            }

            //sort price
            when (sortPriceRadioGroup.checkedRadioButtonId) {
                R.id.lowToHighRadioBtn -> sortPrice = "asc"
                R.id.hightToLowRadioBtn -> sortPrice = "desc"
            }

            //priceRange
            val rangeValue = priceRangeSlider.values
            val range = rangeValue[0].toInt().toString() + "," + rangeValue[1].toInt().toString()

            var selectedTag = ""
            this.selectedTag.forEach {
                selectedTag += it.id + ","
            }

            //size
            productSizeAdapter.selectedIndex.let {
                if (it != -1) {
                    size?.let { list ->
                        selectedSizeId = list[it].id
                    }
                }
            }

            categoriesViewModel.filterProduct(
                categoryId = catId,
                priceSort = sortPrice,
                sizeId = selectedSizeId,
                priceRange = range,
                tags = selectedTag
            )

            saveStatePref()
        }

        categoriesViewModel.category.observe(viewLifecycleOwner, {
            if (category != it.data) {
                category = it.data
                handleCategory(it.data)
            }
        })

        categoriesViewModel.size.observe(viewLifecycleOwner, {
            size = it.data
            handleSize(it.data)
        })

        categoriesViewModel.tag.observe(viewLifecycleOwner, {
            tag = it.data
            handleTag(it.data)
        })
    }

    private fun saveStatePref() {
        pref.edit().clear().apply()
        pref.edit().apply {
            putString(KEY_CATEGORY_SELECTED, catId)
            putInt(KEY_SORT_PRICE_SELECTED, sortPriceRadioGroup.checkedRadioButtonId)
            putString(KEY_SIZE_SELECTED, selectedSizeId)
            putFloat(KEY_PRICE_RANGE_BELOW, priceRangeSlider.values[0])
            putFloat(KEY_PRICE_RANGE_UP, priceRangeSlider.values[1])
            putString(KEY_TAGS_SELECTED, gson.toJson(selectedTag))
        }.apply()
    }

    private fun setUpView() {
        val sortPriceSelected = pref.getInt(KEY_SORT_PRICE_SELECTED, -1)
        if (sortPriceSelected != -1) {
            sortPriceRadioGroup.check(sortPriceSelected)
        }

        val pricesBelow = pref.getFloat(KEY_PRICE_RANGE_BELOW, 0f)
        val pricesUp = pref.getFloat(KEY_PRICE_RANGE_UP, 10000000f)
        priceRangeSlider.values = listOf(pricesBelow, pricesUp)

    }

    private fun handleTag(data: List<Tag>?) {
        if (data.isNullOrEmpty()) {
            tagsGroup.gone()
            return
        }
        tagAdapter.setData(data)

        initTagSelected()

    }

    private fun initTagSelected() {
        val type = object : TypeToken<List<Tag>>() {}.type
        val tagsList: MutableList<Tag> = try {
            gson.fromJson(pref.getString(KEY_TAGS_SELECTED, null), type)
                ?: mutableListOf()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
        if (tagsList.isNotEmpty()) {
            selectedTag.clear()
            selectedTag.addAll(tagsList)
            tagsList.forEach {
                tagAdapter.selectTag(it.id, true)
            }
        }
    }

    private fun handleSize(data: List<Size>?) {
        if (data.isNullOrEmpty()) {
            sizeGroup.gone()
            return
        }
        productSizeAdapter.setData(data)

        initSizeSelected()
    }

    private fun initSizeSelected() {
        val sizeIdSelected = pref.getString(KEY_SIZE_SELECTED, "")
        sizeIdSelected?.let {
            productSizeAdapter.selectedIndex(it)

        }
    }

    private fun handleCategory(data: List<Category>?) {
        if (data.isNullOrEmpty()) {
            categoryGroup.gone()
            return
        }

        if (categoryList.childCount > 0) {
            categoryList.removeAllViews()
        }

        val all = createRadioButton(getString(R.string.filter_tags_all))
        categoryList.addView(all)

        pref.getString(KEY_CATEGORY_SELECTED, "all")?.let {
            catId = it
        }

        data.forEach { category ->
            val radioBtn = createRadioButton(category.name)
            categoryList.addView(radioBtn)

            if (catId == category.id) {
                categoryList.check(radioBtn.id)
            }
        }
    }

    private fun createRadioButton(text: String): RadioButton {
        val radioButton = RadioButton(context)
        radioButton.text = text
        radioButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.palattes_1_50))
        radioButton.textSize = 12f
        return radioButton
    }
}
