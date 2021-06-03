package com.example.arfashion.presentation.app.presentation.product.filter

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.gone
import com.example.arfashion.presentation.app.presentation.main.ui.categories.CategoriesViewModel
import com.example.arfashion.presentation.app.presentation.product.detail.ProductSizeAdapter
import com.example.arfashion.presentation.app.presentation.product.detail.ThumbnailItemOffset
import com.example.arfashion.presentation.data.data
import com.example.arfashion.presentation.data.model.Category
import com.example.arfashion.presentation.data.model.Size
import com.example.arfashion.presentation.data.model.Tag
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.dialog_filter.*

class FilterDialog : DialogFragment() {
    companion object {
        fun newInstance(): FilterDialog {
            return FilterDialog()
        }
    }

    private lateinit var categoriesViewModel : CategoriesViewModel

    private lateinit var productSizeAdapter: ProductSizeAdapter

    private lateinit var tagAdapter: TagFilterAdapter

    private var category: List<Category>? = listOf()

    private var tag: List<Tag>? = listOf()

    private var size: List<Size>? = listOf()

    private var selectedSize: String = ""

    private var sortPrice = "asc"

    private var catId: String = "all"

    private var selectedTag: MutableList<Tag> = mutableListOf()

    private lateinit var filterController: FilterController

    private val gson = GsonBuilder().create()

    init {
        lifecycleScope.launchWhenCreated {
            categoriesViewModel = ViewModelProvider(requireActivity(), MyViewModelFactory(requireContext())).get(
                CategoriesViewModel::class.java)

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

        filterController = FilterController(requireContext())

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
            filterController.clearFilter()
            categoriesViewModel.reset()
            categoriesViewModel.invokeAction()
        }

        applyBtn.setOnClickListener {
            dismiss()

            //category
            val button = view.findViewById<RadioButton>(categoryList.checkedRadioButtonId)
            button?.let {
                when (val catName = button.text) {
                    "all" -> {
                        catId = "all"
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

            var slTag = ""
            selectedTag.forEach {
                slTag += it.name + ","

            }

            //size
            productSizeAdapter.selectedIndex.let {
                if (it != -1) {
                    size?.let { list ->
                        selectedSize = list[it].name
                    }
                }
            }

            categoriesViewModel.reset()
            filterController.saveStateFilter(
                catId,
                sortPrice,
                selectedSize,
                priceRangeSlider.values[0],
                priceRangeSlider.values[1],
                gson.toJson(this.selectedTag)
            )
            categoriesViewModel.invokeAction()
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

    private fun setUpView() {
        val sortPriceSelected = filterController.getSortPrice()
        sortPriceSelected?.let {
            when (it) {
                "desc" -> sortPriceRadioGroup.check(R.id.hightToLowRadioBtn)
                else -> sortPriceRadioGroup.check(R.id.lowToHighRadioBtn)
            }
        }

        val pricesBelow = filterController.getPriceRangeBellow()
        val pricesUp = filterController.getPriceRangeAbove()
        priceRangeSlider.values = listOf(pricesBelow, pricesUp)

    }

    private fun handleTag(data: List<Tag>?) {
        if (data.isNullOrEmpty()) {
            tagsGroup.gone()
            return
        }
        tagAdapter.setData(data)

        initTagSelected(data)

    }

    private fun initTagSelected(data: List<Tag>) {
        val tagsList = filterController.getTagsList()
        selectedTag.clear()
        if (tagsList.isNotEmpty()) {
            selectedTag.addAll(tagsList)
            data.forEach { tag ->
                val tagSelected = tagsList.find { it.id == tag.id }
                if (tagSelected != null) {
                    tagAdapter.selectTag(tagSelected.id, true)
                } else {
                    tagAdapter.selectTag(tag.id, false)
                }
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
        val sizeIdSelected = filterController.getSize()
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

       filterController.getCategory()?.let {
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
