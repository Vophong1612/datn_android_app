package com.example.arfashion.presentation.app.presentation.product.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProductTabAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val count: Int
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return count
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProductDescriptionTabFragment.newInstance()
            else -> ProductReviewTabFragment.newInstance()
        }
    }
}