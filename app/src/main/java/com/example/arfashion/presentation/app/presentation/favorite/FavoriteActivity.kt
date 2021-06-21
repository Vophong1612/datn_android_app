package com.example.arfashion.presentation.app.presentation.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        init()
    }

    override fun onResume() {
        super.onResume()
        initData()
    }

    private fun init(){
        screen_name.text = getString(R.string.favorite_lbl)
        onNavigateBack()

        favoriteViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(FavoriteViewModel::class.java)

        initView()
        initViewModel()
    }

    private fun initData(){
        setStatusView(View.VISIBLE, View.GONE)
        favoriteViewModel.getFavorites()
    }

    private fun setStatusView(v1: Int, v2: Int){
        favorite_v_loading.visibility = v1
        favorite_pb.visibility = v1
        favorite_no_products.visibility = v2
    }

    private fun initView() {

        favoriteAdapter = FavoriteAdapter(this)
        with(favorite_recycler_view){
            adapter = favoriteAdapter
            layoutManager = GridLayoutManager(context, 2)
        }

        favoriteAdapter.deleteProductClickEvent = { product, position ->
            favoriteViewModel.deleteFromFavorite(product.id)
        }
    }

    private fun initViewModel() {

        favoriteViewModel.resultGetFavorites.observe(this) {
            if (it) {
                val response = favoriteViewModel.getFavoritesResponse.value
                if (response != null) {
                    favoriteAdapter.setFavoriteList(response.favourites)
                    setStatusView(View.GONE, View.GONE)
                }
            } else {
                Toast.makeText(this, "Failure!", Toast.LENGTH_SHORT).show()
                setStatusView(View.GONE, View.VISIBLE)
            }
        }

        favoriteViewModel.resultDeleteFromFavorite.observe(this) {
            if (it) {
                val response = favoriteViewModel.deleteFromFavoriteResponse.value
                if (response != null) {
                    initData()
                }
            } else {
                initData()
            }
        }
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }
}