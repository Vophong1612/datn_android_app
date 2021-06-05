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

    private var curr_province_name: String = ""

    private var curr_district_name: String = ""

    private var curr_ward_name: String = ""

    private lateinit var addressViewModel: AddressViewModel

    private val addressService = AddressService.create()

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    companion object {
        var addressStr: String = ""
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
                        provinceList.add(item.province_name)
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
                        districtList.add(item.district_name)
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
                        wardList.add(item.ward_name)
                    initWardSpinner(response.results)
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initView() {
        userManager.currentUser.credential.accessToken?.let { addressService.getProvince(it) }

        check_icon.setOnClickListener {
            if (streetNameEdt.text.toString() != ""){
                addressStr = streetNameEdt.text.toString() + ", " + curr_ward_name +
                        ", " + curr_district_name + ", " + curr_province_name
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
                curr_province_name = provinceList.get(position)
                var temp_province_id = findProvinceId(list,position)
                //setup data for district
                userManager.currentUser.credential.accessToken?.let { addressService.getDistrict(it,
                    temp_province_id.toString()
                ) }
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
                curr_district_name = districtList.get(position)
                var temp_district_id = findDistrictId(list,position)
                //setup data for district
                userManager.currentUser.credential.accessToken?.let { addressService.getWard(it,
                    temp_district_id.toString()
                ) }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun initWardSpinner(results: ArrayList<AddressWardResponse>) {
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_spinner_dropdown_item, wardList)
        wardSpinner.adapter = adapter
        wardSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                curr_ward_name = wardList.get(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
    }

    private fun findProvinceId(list: ArrayList<AddressProvinceResponse>, position: Int): Int {
        for (i in 0 until provinceList.size){
            if (provinceList[position] == list[i].province_name)
                return Integer.parseInt(list[i].province_id);
        }
        return -1;
    }

    private fun findDistrictId(list: ArrayList<AddressDistrictResponse>, position: Int): Int {
        for (i in 0 until list.size){
            if (districtList[position] == list[i].district_name)
                return Integer.parseInt(list[i].district_id);
        }
        return -1;
    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }
}