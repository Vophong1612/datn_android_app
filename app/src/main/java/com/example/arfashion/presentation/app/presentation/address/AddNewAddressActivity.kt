package com.example.arfashion.presentation.app.presentation.address
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.models.address.AddressResponse
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.services.AddressService
import com.example.arfashion.presentation.services.UserService
import com.example.arfashion.presentation.services.Utils
import kotlinx.android.synthetic.main.activity_add_new_address.*
import kotlinx.android.synthetic.main.layout_back_save_header.back_icon
import kotlinx.android.synthetic.main.layout_back_save_header.screen_name
import kotlinx.android.synthetic.main.layout_back_save_white_header.*
import java.util.*

class AddNewAddressActivity : AppCompatActivity() {

    companion object{
        lateinit var addressViewModel: AddressViewModel

        lateinit var userManager: ARFashionUserManager
    }

    private val addressService = AddressService.create()

    private lateinit var pref: SharedPreferences

    private lateinit var userStorage: UserLocalStorage

    private var content: String = ""

    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_address)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.add_address)
        onNavigateBack()
        content = intent.getStringExtra("mode").toString()
        if(content == "add"){
            rl_default_address.visibility = View.INVISIBLE
        }else{
            rl_default_address.visibility = View.VISIBLE
            val obj: AddressResponse = intent.extras?.get("obj") as AddressResponse
            id = obj._id
            fullNameEdt.setText(obj.name)
            phoneNumberEdt.setText(obj.name)
            emailEdt.setText(obj.email)
            addressEdt.setText(obj.home + ", " + obj.village  + ", " + obj.district  + ", " + obj.province)
        }

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
                    userManager.currentUser.credential.accessToken?.let { it1 ->
                        addressViewModel.addAddress(
                            it1, name,
                            email, phone, address, address, address, address)
                    }
                }else{
                    var temp = cb_default_address.isChecked
                    userManager.currentUser.credential.accessToken?.let { it1 ->
                        addressViewModel.updateAddress(
                            it1, name, id,
                            email, phone, temp,
                        address, address, address, address)
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