package com.example.arfashion.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.arfashion.R

class MainActivity : AppCompatActivity() {
    val arr = arrayOf<String>("Tp Hồ Chí Minh, TP Hà Nội, Đà Nẵng")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
//        setUpSpinner()
    }


//    fun setUpSpinner(){
//        langAdapter?.setDropDownViewResource(R.layout.layout_simple_spinner_dropdown)
//        provinceSpinner.adapter = langAdapter
//        districtSpinner.adapter = langAdapter
//        wardSpinner.adapter = langAdapter
//    }

}