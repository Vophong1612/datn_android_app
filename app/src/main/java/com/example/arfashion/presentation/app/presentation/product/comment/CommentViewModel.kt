package com.example.arfashion.presentation.app.presentation.product.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.arfashion.presentation.app.models.product.GetCommentsResponse
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.Comment
import com.example.arfashion.presentation.services.CommentService
import com.example.arfashion.presentation.services.toComment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentViewModel : ViewModel(){

    private val commentService = CommentService.create()

    private val _comment = MutableLiveData<ARResult<List<Comment>>>()
    val comment: LiveData<ARResult<List<Comment>>>
        get() = _comment

    private val _total = MutableLiveData<ARResult<Int>>()
    val total : LiveData<ARResult<Int>> = _total

    fun getComment(productId: String) {
        commentService.getComments(productId).enqueue(object: Callback<GetCommentsResponse> {
            override fun onResponse(call: Call<GetCommentsResponse>, response: Response<GetCommentsResponse>) {
                when (response.code()) {
                    200 -> {
                        response.body()?.let { it ->
                            _comment.value = ARResult.Success(it.comment.map{ comment ->
                                comment.toComment()
                            })
                            _total.value = ARResult.Success(it.totals)
                        }
                    }
                    else ->  {
                        _comment.value = ARResult.Error(Throwable("Can not get comments. Code: ${response.code()}"))
                        _total.value = ARResult.Error(Throwable("Can not get comments. Code: ${response.code()}"))
                    }
                }
            }

            override fun onFailure(call: Call<GetCommentsResponse>, t: Throwable) {
                _comment.value = ARResult.Error(t)
                _total.value = ARResult.Error(t)
            }

        })
    }
}