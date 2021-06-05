package com.example.arfashion.presentation.app.presentation.main.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.product.CategoriesResponse
import com.example.arfashion.presentation.app.models.product.ProductByConditionResponse
import com.example.arfashion.presentation.app.models.product.Sizes
import com.example.arfashion.presentation.app.models.product.Tags
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Category
import com.example.arfashion.presentation.data.model.Product
import com.example.arfashion.presentation.data.model.Size
import com.example.arfashion.presentation.data.model.Tag
import com.example.arfashion.presentation.services.ProductService
import com.example.arfashion.presentation.services.toCategory
import com.example.arfashion.presentation.services.toProduct
import com.example.arfashion.presentation.services.toTag
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesViewModel : ViewModel() {
    private val productService = ProductService.create()

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _categories = MutableLiveData<ARResult<List<Category>>>()
    val category: LiveData<ARResult<List<Category>>> = _categories

    private val _tag = MutableLiveData<ARResult<List<Tag>>>()
    val tag: LiveData<ARResult<List<Tag>>> = _tag

    private val _size = MutableLiveData<ARResult<List<Size>>>()
    val size: LiveData<ARResult<List<Size>>> = _size

    private val _search = MutableLiveData<ARResult<List<Product>>>()
    val search: LiveData<ARResult<List<Product>>> = _search

    private val _listProductByCategory = MutableLiveData<ARResult<List<Product>>>()
    val listProductByCategory: LiveData<ARResult<List<Product>>> = _listProductByCategory

    private val _filter = MutableLiveData<ARResult<List<Product>>>()
    val filter : LiveData<ARResult<List<Product>>> = _filter

    fun getCategories() {
        _loading.value = true
        productService.getCategories().enqueue(object : Callback<List<CategoriesResponse>> {
            override fun onResponse(
                call: Call<List<CategoriesResponse>>,
                response: Response<List<CategoriesResponse>>
            ) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _categories.value = ARResult.Success(it.map { category ->
                                category.toCategory()
                            })
                        }
                    }
                    else -> _categories.value =
                        ARResult.Error(Throwable("Fail to get category. Code: ${response.code()}"))
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<List<CategoriesResponse>>, t: Throwable) {
                _loading.value = false
                _categories.value = ARResult.Error(t)
            }
        })
    }

    fun getTag() {
        productService.getTags().enqueue(object : Callback<List<Tags>> {
            override fun onResponse(call: Call<List<Tags>>, response: Response<List<Tags>>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _tag.value = ARResult.Success(it.map { tag ->
                                tag.toTag()
                            })
                        }
                    }
                    else -> _categories.value =
                        ARResult.Error(Throwable("Fail to get tags. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Tags>>, t: Throwable) {
                _tag.value = ARResult.Error(t)
            }

        })
    }

    fun getSize() {
        productService.getSizes().enqueue(object : Callback<List<Sizes>> {
            override fun onResponse(call: Call<List<Sizes>>, response: Response<List<Sizes>>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let {
                            _size.value = ARResult.Success(it.map { size ->
                                Size(id = size._id, name = size.name)
                            })
                        }
                    }
                    else -> _categories.value =
                        ARResult.Error(Throwable("Fail to get tags. Code: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<List<Sizes>>, t: Throwable) {
                _size.value = ARResult.Error(t)
            }

        })
    }

    fun searchByKeyWord(keyword: String) {
        _loading.value = true
        productService.getProductByCondition(keyword = keyword)
            .enqueue(object : Callback<ProductByConditionResponse> {
                override fun onResponse(
                    call: Call<ProductByConditionResponse>,
                    response: Response<ProductByConditionResponse>
                ) {
                    _loading.value = false
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                _search.value = ARResult.Success(it.product.map { product ->
                                    product.toProduct()
                                })
                            }
                        }
                        else -> _search.value =
                            ARResult.Error(Throwable("Fail to search. Code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ProductByConditionResponse>, t: Throwable) {
                    _loading.value = false
                    _search.value = ARResult.Error(t)
                }

            })
    }

    fun getProductListByCategory(id: String) {
        _loading.value = true
        productService.getProductByCondition(categoryId = id)
            .enqueue(object : Callback<ProductByConditionResponse> {
                override fun onResponse(
                    call: Call<ProductByConditionResponse>,
                    response: Response<ProductByConditionResponse>
                ) {
                    _loading.value = false
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                _listProductByCategory.value =
                                    ARResult.Success(it.product.map { product ->
                                        product.toProduct()
                                    })
                            }
                        }
                        else -> _listProductByCategory.value =
                            ARResult.Error(Throwable("Fail to get list product by category. Code: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<ProductByConditionResponse>, t: Throwable) {
                    _loading.value = false
                    _listProductByCategory.value = ARResult.Error(t)
                }
            })
    }

    fun filterProduct(
        keyword: String = "",
        time: String = "asc",
        categoryId: String = "all",
        priceRange: String = "",
        priceSort: String = "asc",
        sizeId: String = "",
        tags: String = "",
        limit: Int = 12,
        offset: Int = 0
    ) {
        _loading.value = true
        productService.getProductByCondition(
            keyword,
            time,
            categoryId,
            priceRange,
            priceSort,
            sizeId,
            tags,
            limit,
            offset
        )
            .enqueue(object : Callback<ProductByConditionResponse> {
                override fun onResponse(
                    call: Call<ProductByConditionResponse>,
                    response: Response<ProductByConditionResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                _filter.value = ARResult.Success(it.product.map { product ->
                                    product.toProduct()
                                })
                            }
                        }
                        else -> _filter.value =
                            ARResult.Error(Throwable("Fail to search. Code: ${response.code()}"))
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<ProductByConditionResponse>, t: Throwable) {
                    _filter.value = ARResult.Error(t)
                    _loading.value = false
                }

            })
    }
}