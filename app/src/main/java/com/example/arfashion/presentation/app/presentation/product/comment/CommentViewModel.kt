package com.example.arfashion.presentation.app.presentation.product.comment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.product.GetCommentsResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Comment
import com.example.arfashion.presentation.services.CommentService
import com.example.arfashion.presentation.services.toComment
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel(context: Context) : ViewModel() {

    private val commentService = CommentService.create(context)

    private val _comment = MutableLiveData<ARResult<List<Comment>>>()
    val comment: LiveData<ARResult<List<Comment>>>
        get() = _comment

    private val _addComment = MutableLiveData<ARResult<Boolean>>()
    val addComment: LiveData<ARResult<Boolean>>
        get() = _addComment

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _total = MutableLiveData<ARResult<Int>>()
    val total: LiveData<ARResult<Int>> = _total

    private val _offset = MutableLiveData<ARResult<Int>>()
    val offset: LiveData<ARResult<Int>> = _offset

    private val _currentPage = MutableLiveData(0)
    private val _totalPage = MutableLiveData(0)

    var productId: String = ""

    fun getComment(productId: String, offset: Int = 0, limit: Int = 10) {
        if (offset != 0) {
            if (_currentPage.value!! >= _totalPage.value!!) {
                return
            }
        } else {
            reset()
        }
        this.productId = productId
        _loading.value = true
        commentService.getComments(productId, offset = offset, limit = limit)
            .enqueue(object : Callback<GetCommentsResponse> {
                override fun onResponse(
                    call: Call<GetCommentsResponse>,
                    response: Response<GetCommentsResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let { it ->
                                _comment.value = ARResult.Success(it.comment.map { comment ->
                                    comment.toComment()
                                })
                                _total.value = ARResult.Success(it.totals)
                                _offset.value = ARResult.Success(it.offset + it.limit)
                                _currentPage.value = it.currentPage
                                _totalPage.value = it.totalPage
                            }
                        }
                        else -> {
                            _comment.value =
                                ARResult.Error(Throwable("Can not get comments. Code: ${response.code()}"))
                            _total.value = ARResult.Error(Throwable())
                            _offset.value = ARResult.Error(Throwable())
                        }
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                    _comment.value = ARResult.Error(t)
                    _total.value = ARResult.Error(t)
                    _offset.value = ARResult.Error(t)
                    _loading.value = false
                }

            })
    }

    fun addComment(
        photos: List<MultipartBody.Part>, star: RequestBody, content: RequestBody,
        title: RequestBody, productId: RequestBody
    ) {
        _loading.value = true
        commentService.addComment(photos, star, content, title, productId)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    when (response.code()) {
                        200 -> {
                            response.body()?.let {
                                _addComment.value = ARResult.Success(true)
                            }
                        }
                        else -> _addComment.value =
                            ARResult.Error(Throwable("Fail to add comment !. Code: ${response.code()}"))
                    }
                    _loading.value = false
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    _addComment.value = ARResult.Error(t)
                    _loading.value = false
                }
            })
    }

    private fun reset() {
        _offset.value = ARResult.Success(0)
        _totalPage.value = 0
        _currentPage.value = 0
    }
}