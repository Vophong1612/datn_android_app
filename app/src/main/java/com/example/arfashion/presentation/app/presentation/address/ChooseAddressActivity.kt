package com.example.arfashion.presentation.app.presentation.address

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.models.address.*
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.services.AddressService
import kotlinx.android.synthetic.main.activity_choose_address.*
import kotlinx.android.synthetic.main.layout_back_save_header.*

class ChooseAddressActivity : AppCompatActivity() {

    private var provinceList: ArrayList<String> = ArrayList()

    private var districtList: ArrayList<String> = ArrayList()

    private var wardList: ArrayList<String> = ArrayList()

    private lateinit var addressViewModel: AddressViewModel

    private val addressService = AddressService.create()

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private var token: String = ""

    companion object {

        var curr_province_name: String = ""

        var curr_province_code: Int = -1

        var curr_district_name: String = ""

        var curr_district_code: Int = -1

        var curr_ward_name: String = ""

        var curr_ward_code: Int = -1

        var curr_home_name : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_address)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.choose_address)
        onNavigateBack()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        token = pref.getString("pref_access_token", "").toString()

        addressViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AddressViewModel(addressService) as T
            }
        })[AddressViewModel::class.java]

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        addressViewModel.resultChooseProvince.observe(this) {
            if (it) {
                val response = addressViewModel.chooseProvinceResponse.value
                if (response != null) {
                    for (item : AddressProvinceResponse in response.results)
                        provinceList.add(item.name)
                        initProvinceSpinner(response.results)
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }

        addressViewModel.resultChooseDistrict.observe(this) {
            if (it) {
                val response = addressViewModel.chooseDistrictResponse.value
                if (response != null) {
                    if (districtList.size > 0)
                        districtList.clear()
                    for (item : AddressDistrictResponse in response.results)
                        districtList.add(item.name)
                    initDistrictSpinner(response.results)
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }

        addressViewModel.resultChooseVillage.observe(this) {
            if (it) {
                val response = addressViewModel.chooseVillageResponse.value
                if (response != null) {
                    if (wardList.size > 0)
                        wardList.clear()
                    for (item : AddressWardResponse in response.results)
                        wardList.add(item.name)
                    initWardSpinner(response.results)
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        addressViewModel.chooseProvince(token)

        check_icon.setOnClickListener {
            if (streetNameEdt.text.toString() != ""){
                curr_home_name = streetNameEdt.text.toString()
                finish()
            } else
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initProvinceSpinner(list: ArrayList<AddressProvinceResponse>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, provinceList)
        provinceSpinner.adapter = adapter
        provinceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                curr_province_name = provinceList[position]
                var temp_province_id = findProvinceId(list,position)
                curr_province_code = temp_province_id
                //setup data for district
                addressViewModel.chooseDistrict(token, temp_province_id.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initDistrictSpinner(list: ArrayList<AddressDistrictResponse>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, districtList)
        districtSpinner.adapter = adapter
        districtSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                curr_district_name = districtList[position]
                var temp_district_id = findDistrictId(list,position)
                curr_district_code = temp_district_id
                //setup data for district
                addressViewModel.chooseVillage(token, temp_district_id.toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initWardSpinner(list: ArrayList<AddressWardResponse>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, wardList)
        wardSpinner.adapter = adapter
        wardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                curr_ward_name = wardList[position]
                var temp_ward_id = findWardId(list,position)
                curr_ward_code = temp_ward_id
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun findWardId(list: ArrayList<AddressWardResponse>, position: Int): Int {
        for (i in 0 until list.size){
            if (wardList[position] == list[i].name)
                return Integer.parseInt(list[i].code)
        }
        return -1
    }

    private fun findProvinceId(list: ArrayList<AddressProvinceResponse>, position: Int): Int {
        for (i in 0 until provinceList.size){
            if (provinceList[position] == list[i].name)
                return Integer.parseInt(list[i].code)
        }
        return -1
    }

    private fun findDistrictId(list: ArrayList<AddressDistrictResponse>, position: Int): Int {
        for (i in 0 until list.size){
            if (districtList[position] == list[i].name)
                return Integer.parseInt(list[i].code)
        }
        return -1
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }
}