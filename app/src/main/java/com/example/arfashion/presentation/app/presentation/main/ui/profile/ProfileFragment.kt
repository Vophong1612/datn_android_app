package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.MyViewModelFactory
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.bill.BillActivity
import com.example.arfashion.presentation.app.presentation.cart.CartActivity
import com.example.arfashion.presentation.app.presentation.favorite.FavoriteActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.fragment_profile_tab.*
import kotlinx.android.synthetic.main.fragment_profile_tab.refreshLayout

class ProfileFragment : Fragment() {

    private lateinit var user: User

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private lateinit var profileViewModel: ProfileViewModel

    companion object {
        const val TAG = "ProfileFragment"

        fun newInstance(): ProfileFragment {
            val args = Bundle()

            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_profile_tab, container, false)
    }

    override fun onResume() {
        super.onResume()

        profileViewModel.getProfile()
        initView()
        initViewModel()
    }

    private fun initView() {

        rl_user_favorite.setOnClickListener {
            val intent = Intent(this.requireContext(), FavoriteActivity::class.java)
            startActivity(intent)
        }

        rl_user_cart.setOnClickListener {
            val intent = Intent(this.requireContext(), CartActivity::class.java)
            startActivity(intent)
        }

        rl_user_management.setOnClickListener {
            val intent = Intent(this.requireContext(), BillActivity::class.java)
            startActivity(intent)
        }

        tv_sign_out.setOnClickListener {
            logOut()
        }

        refreshLayout.setOnRefreshListener {
            profileViewModel.getProfile()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(this, MyViewModelFactory(this.requireContext())).get(ProfileViewModel::class.java)

        pref = this.requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        userStorage = UserLocalStorage(pref)
        userManager = ARFashionUserManager(userStorage).getInstance()
        user = userStorage.load()

        iv_transform_user_detail.setOnClickListener {
            val intent = Intent(this.requireContext(), ProfileDetailActivity::class.java)
            startActivity(intent)
        }

    }

    private fun initViewModel() {
        profileViewModel.resultGetProfile.observe(this.requireActivity()) {
            if (it) {
                val response = profileViewModel.getProfileResponse.value
                if (response != null) {
                    var resEmail: String = response.email
                    var resPhone: String = response.phone
                    var resAvt: String = response.avatar
                    var resBirthday: String = response.birthday
                    var resGender: Int = response.gender

                    if(response.email.isNullOrBlank()) resEmail = ""
                    if(response.phone.isNullOrBlank()) resPhone = ""
                    if(response.avatar.isNullOrBlank()) resAvt = ""
                    if(response.birthday.isNullOrBlank()) resBirthday = ""
                    if(response.gender.toString().isNullOrBlank()) resGender = -1

                    userManager.currentUser = User(Profile(
                        response._id, response.name, resEmail, resPhone,
                        resAvt, resGender, resBirthday, response.account_status
                    ), Credential(user.credential.accessToken))
                    userStorage.save(userManager.currentUser)
                    user = userStorage.load()
                    setDataProfile()
                    refreshLayout.isRefreshing = false
                }
            } else {
                Toast.makeText(this.requireContext(), "Failure!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDataProfile() {
        tv_name_general.text = user.profile.name

        if(!user.profile.avatar.isNullOrEmpty()){
            Glide.with(iv_avatar_general)
                .load(user.profile.avatar)
                .into(iv_avatar_general)
        }
    }

    private fun logOut() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle(getString(R.string.dialog_confirm))
        builder.setMessage(getString(R.string.alert_sign_out))

        builder.setPositiveButton(
            getString(R.string.dialog_yes)
        ) { dialog, _ -> // Do nothing but close the dialog
            dialog.dismiss()
            userStorage.save(User(Profile(), Credential()))
            this.requireActivity().finish()
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