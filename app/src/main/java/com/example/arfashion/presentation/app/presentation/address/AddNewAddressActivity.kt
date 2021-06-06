package com.example.arfashion.presentation.app.presentation.address

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.AddressService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_add_new_address.*
import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name
import kotlinx.android.synthetic.main.layout_back_save_white_header.*

class AddNewAddressActivity : AppCompatActivity() {

    private lateinit var addressViewModel: AddressViewModel

    private lateinit var userManager: ARFashionUserManager

    private val addressService = AddressService.create()

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private lateinit var user: User

    private var content: String = ""

    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_address)
        init()
    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        if(content == "add"){
            if(ChooseAddressActivity.curr_home_name.isNotEmpty()){
                addressEdt.setText(ChooseAddressActivity.curr_home_name + ", "
                        + ChooseAddressActivity.curr_ward_name  + ", "
                        + ChooseAddressActivity.curr_district_name  + ", "
                        + ChooseAddressActivity.curr_province_name)
            }
        }else{
            if(ChooseAddressActivity.curr_ward_code != -1){
                addressEdt.setText(ChooseAddressActivity.curr_home_name + ", "
                        + ChooseAddressActivity.curr_ward_name  + ", "
                        + ChooseAddressActivity.curr_district_name  + ", "
                        + ChooseAddressActivity.curr_province_name)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun init() {

        screen_name.text = this.getString(R.string.add_address)
        onNavigateBack()

        pref = applicationContext.getSharedPreferences("user", MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        user = userStorage.load()
        content = intent.getStringExtra("mode").toString()

        if(content == "add"){
            rl_default_address.visibility = View.INVISIBLE
        }else{
            rl_default_address.visibility = View.VISIBLE
            val objId = intent.getSerializableExtra("obj.id").toString()
            val objName = intent.getSerializableExtra("obj.name").toString()
            val objEmail = intent.getSerializableExtra("obj.email").toString()
            val objPhone = intent.getSerializableExtra("obj.phone").toString()
            val objHome = intent.getSerializableExtra("obj.home").toString()
            val objVillage = intent.getSerializableExtra("obj.village").toString()
            val objDistrict = intent.getSerializableExtra("obj.district").toString()
            val objProvince = intent.getSerializableExtra("obj.province").toString()
            val objIsDefault = intent.getSerializableExtra("obj.isDefault").toString()

            ChooseAddressActivity.curr_home_name = objHome
            if(ChooseAddressActivity.curr_ward_code != -1){
                ChooseAddressActivity.curr_ward_code = intent.getSerializableExtra("obj.villageCode").toString().toInt()
                ChooseAddressActivity.curr_district_code = intent.getSerializableExtra("obj.districtCode").toString().toInt()
                ChooseAddressActivity.curr_province_code = intent.getSerializableExtra("obj.provinceCode").toString().toInt()
            }

            id = objId
            fullNameEdt.setText(objName)
            phoneNumberEdt.setText(objPhone)
            emailEdt.setText(objEmail)
            addressEdt.setText("$objHome, $objVillage, $objDistrict, $objProvince")
            if(objIsDefault.toBoolean())
                cb_default_address.isChecked = true
        }

        addressViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AddressViewModel(addressService) as T
            }
        })[AddressViewModel::class.java]

        initView()
        initViewModel()
    }

    private fun initView() {

        arrowChooseAddress.setOnClickListener {
            val intent = Intent(this, ChooseAddressActivity::class.java)
            startActivity(intent)
        }

        check_icon.setOnClickListener {
            val name: String = fullNameEdt.text.toString()
            val phone: String = phoneNumberEdt.text.toString()
            val email: String = emailEdt.text.toString()
            val address: String = addressEdt.text.toString()

            if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()){
                Toast.makeText(this, getString(R.string.invalid_data), Toast.LENGTH_SHORT).show()
            }else if(!Utils.isValidEmail(email)){
                Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            }else if(!Utils.isValidPhone(phone)){
                Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show()
            }else {
                if(content == "add"){
                    user.credential.accessToken?.let { it1 ->
                        addressViewModel.addAddress(
                            it1, name, email, phone,
                            ChooseAddressActivity.curr_home_name,
                            ChooseAddressActivity.curr_ward_code,
                            ChooseAddressActivity.curr_district_code,
                            ChooseAddressActivity.curr_province_code)
                    }
                }else{
                    val temp = cb_default_address.isChecked
                    user.credential.accessToken?.let { it1 ->
                        addressViewModel.updateAddress(
                            it1, id, name, email, phone, temp,
                            ChooseAddressActivity.curr_home_name,
                            ChooseAddressActivity.curr_ward_code,
                            ChooseAddressActivity.curr_district_code,
                            ChooseAddressActivity.curr_province_code)
                    }

                }
            }
        }
    }

    private fun initViewModel() {
        addressViewModel.resultAddAddress.observe(this) {
            if (it) {
                val response = addressViewModel.addAddressResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }

        addressViewModel.resultUpdateAddress.observe(this) {
            if (it) {
                val response = addressViewModel.updateAddressResponse.value
                if (response != null) {
                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Failure !", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun onNavigateBack() {
        back_icon.setOnClickListener {
            finish()
        }
    }

}