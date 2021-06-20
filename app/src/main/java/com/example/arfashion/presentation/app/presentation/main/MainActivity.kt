package com.example.arfashion.presentation.app.presentation.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.main.ui.home.HomeFragment
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity() {

    private lateinit var userManager: ARFashionUserManager

    private lateinit var userStorage: UserLocalStorage

    private lateinit var pref: SharedPreferences

    private lateinit var user: User

    private lateinit var mainPagerAdapter: MainPagerAdapter

    private val homeToCategoriesShareViewModel: HomeToCategoriesShareViewModel by viewModels()

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

    override fun onBackPressed() {
        pref = this.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        user = userStorage.load()

        if(!user.credential.accessToken.isNullOrEmpty()){
            quit()
        }
    }

    private fun quit() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.dialog_confirm))
        builder.setMessage(getString(R.string.alert_exit_app))

        builder.setPositiveButton(
            getString(R.string.dialog_yes)
        ) { dialog, _ -> // Do nothing but close the dialog
            dialog.dismiss()
            finishAffinity()
        }

        builder.setNegativeButton(
            getString(R.string.dialog_no)
        ) { dialog, _ -> // Do nothing
            dialog.dismiss()
        }

        val alert: AlertDialog = builder.create()
        alert.show()
    }
}