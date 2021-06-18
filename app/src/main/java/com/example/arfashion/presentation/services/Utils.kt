package com.example.arfashion.presentation.services

import android.text.TextUtils
import android.util.Patterns
import java.text.NumberFormat
import java.util.*
import com.example.arfashion.presentation.data.model.Profile

class Utils {
    companion object{
        fun isValidEmail(target: String?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.EMAIL_ADDRESS.matcher(target).matches();
            }
        }

        fun isValidPassword(target: String?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                target!!.length > 6
            }
        }

        fun isValidName(target: String?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                target!!.length > 8
            }
        }

        fun isValidPhone(target: String?): Boolean {
            return if (TextUtils.isEmpty(target)) {
                false
            } else {
                Patterns.PHONE.matcher(target).matches();
            }
        }

        fun formatPhone(target: String?): String {
           val res: String? = target
            if (res != null) {
                return "84" + res.removeRange(0,1)
            }
            return ""
        }

        fun formatPrice(target: Int?): String {

            val format: NumberFormat = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("EUR")

            return format.format(target).removeRange(0, 1)
        }

        fun initData(user: Profile) {
            if(user.email.isNullOrEmpty()) user.email = ""
            if(user.phone.isNullOrEmpty()) user.phone = ""
            if(user.birthday.isNullOrEmpty()) user.birthday = ""
        }

        fun formatDate(target: String): String {
            return target.substring(6, 10) + "-" + target.substring(3,5) + "-" + target.substring(0,2) + "T00:00:00.000Z"
        }

        fun formatDateToString(target: String): String {
            return target.substring(8,10) + "/" + target.substring(5,7) + "/" + target.substring(0,4)

        }
    }

}