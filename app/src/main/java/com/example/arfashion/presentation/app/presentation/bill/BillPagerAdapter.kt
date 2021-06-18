package com.example.arfashion.presentation.app.presentation.bill

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.arfashion.presentation.data.model.BillStatus

class BillPagerAdapter(
    fragmentManager: FragmentManager,
    lifeCycle: Lifecycle,
    private val billList: List<BillStatus>
) : FragmentStateAdapter(fragmentManager, lifeCycle) {

    fun getPageTitle(position: Int): String {
        return billList[position].name
    }

    fun getIndexPageOfPackage(billId: String): Int {
        billList.forEachIndexed { index, billStatus ->
            if (billStatus.id == billId) {
                return index
            }
        }
        return -1
    }

    override fun getItemCount(): Int {
        return billList.size
    }

    override fun createFragment(position: Int): Fragment {
        return BillFragment.newInstance(
            billList[position].id,
            billList[position].name
        )
    }
}