package com.example.arfashion.presentation.app.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.arfashion.presentation.app.presentation.main.ui.categories.CategoriesFragment
import com.example.arfashion.presentation.app.presentation.main.ui.home.HomeFragment
import com.example.arfashion.presentation.app.presentation.main.ui.profile.NotificationsFragment

class MainPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val count: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            TabPosition.Categories.index -> CategoriesFragment.newInstance()
            TabPosition.Profile.index -> NotificationsFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }

}