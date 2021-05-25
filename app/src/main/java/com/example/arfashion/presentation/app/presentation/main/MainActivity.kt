package com.example.arfashion.presentation.app.presentation.main

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.main.ui.dashboard.DashboardFragment
import com.example.arfashion.presentation.app.presentation.main.ui.home.HomeFragment
import com.example.arfashion.presentation.app.presentation.main.ui.notifications.NotificationsFragment
import com.example.arfashion.presentation.data.ARFashionUserManager
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity() {
    private lateinit var userManager: ARFashionUserManager
    private lateinit var userStorage: UserLocalStorage
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )

       toHomeFragment()
        nav_view.setMenuResource(R.menu.bottom_nav_menu)
        nav_view.setOnItemSelectedListener { id ->
            when (id) {
                R.id.navigation_home -> toHomeFragment()
                R.id.navigation_dashboard -> toDashboardFragment()
                R.id.navigation_notifications -> toNotificationsFragment()
            }
        }
    }

    private fun toHomeFragment() {
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, HomeFragment.newInstance())
            addToBackStack(HomeFragment.TAG)
            commit()
        }
    }

    private fun toDashboardFragment() {
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, DashboardFragment.newInstance())
            addToBackStack(DashboardFragment.TAG)
            commit()
        }
    }

    private fun toNotificationsFragment() {
        val fragmentManager = this.supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment, NotificationsFragment.newInstance())
            addToBackStack(NotificationsFragment.TAG)
            commit()
        }
    }
}