package com.example.arfashion.presentation.app.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arfashion.R
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainPagerAdapter: MainPagerAdapter

    private val homeToCategoriesShareViewModel: HomeToCategoresShareViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (savedInstanceState == null) {
            nav_view.setItemSelected(R.id.navigation_home)
        }

        mainPagerAdapter = MainPagerAdapter(supportFragmentManager, lifecycle, nav_view.childCount)
        mainPager.adapter = mainPagerAdapter
        mainPager.isUserInputEnabled = false
        mainPager.currentItem = nav_view.getSelectedItemId()
        mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> nav_view.setItemSelected(R.id.navigation_category)
                    2 -> nav_view.setItemSelected(R.id.navigation_profile)
                    else -> nav_view.setItemSelected(R.id.navigation_home)
                }
            }
        })

        nav_view.setOnItemSelectedListener { id ->
            when (id) {
                R.id.navigation_home -> mainPager.currentItem = TabPosition.Home.index
                R.id.navigation_category -> mainPager.currentItem = TabPosition.Categories.index
                R.id.navigation_profile -> mainPager.currentItem = TabPosition.Profile.index
            }
        }

        homeToCategoriesShareViewModel.searchClickEvent.observe(this) {
            mainPager.currentItem = TabPosition.Categories.index
            nav_view.setItemSelected(R.id.navigation_category)
        }
    }
}