package com.example.arfashion.presentation.app.presentation.bill

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.data.ARResult
import com.example.arfashion.presentation.data.model.BillStatus
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_bill.*
import kotlinx.android.synthetic.main.layout_back_header.*

class BillActivity : AppCompatActivity() {

    private lateinit var tabMediator: TabLayoutMediator

    private var billPagerAdapter: BillPagerAdapter? = null

    private lateinit var billViewModel: BillViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        init()
    }

    private fun init() {
        screen_name.text = getString(R.string.order_management_screen_name)
        back_icon.setOnClickListener {
            finish()
        }

        billViewModel = ViewModelProvider(this, MyViewModelFactory(applicationContext)).get(BillViewModel::class.java)

        billViewModel.getBillStatus()

        billViewModel.billStatus.observe(this) {
            when (it) {
                is ARResult.Success -> {
                    initTabLayout(it.data)
                }
                is ARResult.Error -> Toast.makeText(
                    applicationContext,
                    it.throwable.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initTabLayout(list: List<BillStatus>) {
        billPagerAdapter = BillPagerAdapter(supportFragmentManager, lifecycle, list)
        billPager.adapter = billPagerAdapter
        billPager.isUserInputEnabled = false
        billPagerAdapter?.let {
            tabMediator = TabLayoutMediator(tabLayout, billPager) { tab, position ->
                tab.text = it.getPageTitle(position)
            }
            tabMediator.attach()
        }
    }
}