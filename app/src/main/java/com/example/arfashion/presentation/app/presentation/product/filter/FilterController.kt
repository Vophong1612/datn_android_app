package com.example.arfashion.presentation.app.presentation.product.filter

import android.content.Context
import com.example.arfashion.presentation.data.model.Tag
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

private const val KEY_SHARED_PREFERENCES = "key_shared_preferences_filter_state"
private const val KEY_CATEGORY_SELECTED = "key_category_selected"
private const val KEY_SORT_PRICE_SELECTED = "key_sort_price_selected"
private const val KEY_SIZE_SELECTED = "key_sized_selected"
private const val KEY_PRICE_RANGE_BELOW = "key_price_range_below"
private const val KEY_PRICE_RANGE_UP = "key_price_range_up"
private const val KEY_TAGS_SELECTED = "key_tags_selected"

class FilterController(
    private val context: Context
) {
    private val pref by lazy {
        context.getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    private val gson = GsonBuilder().create()

    fun saveStateFilter(
        categoryId: String,
        sortPrice: String,
        sizeId: String,
        priceRangeBellow: Float,
        priceRangeAbove: Float,
        tags: String
    ) {
        clearFilter()
        pref.edit().apply {
            putString(KEY_CATEGORY_SELECTED, categoryId)
            putString(KEY_SORT_PRICE_SELECTED, sortPrice)
            putString(KEY_SIZE_SELECTED, sizeId)
            putFloat(KEY_PRICE_RANGE_BELOW, priceRangeBellow)
            putFloat(KEY_PRICE_RANGE_UP, priceRangeAbove)
            putString(KEY_TAGS_SELECTED, tags)
        }.apply()
    }

    fun clearFilter() {
        pref.edit().clear().apply()
    }

    fun getSortPrice(): String? {
        return pref.getString(KEY_SORT_PRICE_SELECTED, "")
    }

    fun getPriceRangeBellow(): Float {
        return pref.getFloat(KEY_PRICE_RANGE_BELOW, 0f)
    }

    fun getPriceRangeAbove(): Float {
        return pref.getFloat(KEY_PRICE_RANGE_UP, 3000000f)
    }

    fun getTagsList(): MutableList<Tag> {
        val type = object : TypeToken<List<Tag>>() {}.type
        return try {
            gson.fromJson(pref.getString(KEY_TAGS_SELECTED, null), type)
                ?: mutableListOf()
        } catch (e: Exception) {
            e.printStackTrace()
            mutableListOf()
        }
    }

    fun getSize(): String? {
        return pref.getString(KEY_SIZE_SELECTED, "")
    }

    fun getCategory(): String? {
        return pref.getString(KEY_CATEGORY_SELECTED, "all")
    }
}