package com.example.arfashion.presentation.app.presentation.main.ui.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.bumptech.glide.Glide
import com.example.arfashion.R
import com.example.arfashion.presentation.app.local.UserLocalStorage
import com.example.arfashion.presentation.app.presentation.address.AddressListActivity
import com.example.arfashion.presentation.data.ARFashionUserManager
import com.example.arfashion.presentation.data.credential.Credential
import com.example.arfashion.presentation.data.model.Profile
import com.example.arfashion.presentation.data.model.User
import com.example.arfashion.presentation.services.UserService
import kotlinx.android.synthetic.main.fragment_profile_tab.*
import kotlinx.android.synthetic.main.layout_before_test.*

class ProfileFragment : Fragment() {

    private lateinit var user: User

    private lateinit var userStorage: UserLocalStorage

    private lateinit var userManager: ARFashionUserManager

    private lateinit var pref: SharedPreferences

    private lateinit var profileViewModel: ProfileViewModel

    private val userService = UserService.create()

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
        user.credential.accessToken?.let { profileViewModel.getProfile(it) }
        initViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(userService) as T
            }
        })[ProfileViewModel::class.java]

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
                    userManager.currentUser = User(com.example.arfashion.presentation.data.model.Profile(
                        response._id, response.name, response.email, response.phone, response.avatar,
                        response.gender, response.birthday, response.account_status
                    ), Credential(user.credential.accessToken))
                    userStorage.save(userManager.currentUser)
                    user = userStorage.load()
                    setDataProfile()
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

        tv_sign_out.setOnClickListener {
            logOut()
        }
    }

    private fun logOut() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())

        builder.setTitle("Confirm")
        builder.setMessage("Are you sure to sign out?")

        builder.setPositiveButton(
            "YES",
            DialogInterface.OnClickListener { dialog, _ -> // Do nothing but close the dialog
                dialog.dismiss()
               userStorage.save(User(Profile(), Credential()))
                this.requireActivity().finish()
            })

        builder.setNegativeButton(
            "NO",
            DialogInterface.OnClickListener { dialog, _ -> // Do nothing
                dialog.dismiss()
            })

        val alert: AlertDialog = builder.create()
        alert.show()
    }
}