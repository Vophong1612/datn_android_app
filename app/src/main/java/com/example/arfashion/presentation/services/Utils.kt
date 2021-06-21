package com.example.arfashion.presentation.services

import android.text.TextUtils
import android.util.Patterns
import com.example.arfashion.presentation.data.model.Profile
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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
            if (!res.isNullOrEmpty()) {
                return "84" + res.removeRange(0,1)
            }
            return ""
        }

        fun formatPhoneToNormal(target: String?): String {
            val res: String? = target
            if (!res.isNullOrEmpty()) {
                return "0" + res.removeRange(0,2)
            }
            return ""
        }

        fun initData(user: Profile) {
            if(user.email.isNullOrEmpty()) user.email = ""
            if(user.phone.isNullOrEmpty()) user.phone = ""
            if(user.gender.toString().isNullOrEmpty()) user.gender = -1
            if(user.birthday.isNullOrEmpty()) user.birthday = ""
            if(user.avatar.isNullOrEmpty()) user.avatar = ""
            if(user.gender.toString().isNullOrEmpty()) user.gender = -1
            if(user.avatar.isNullOrEmpty()) user.avatar = ""
        }

        fun formatDate(target: String): String {
            if(target.isNotEmpty())
                return target.substring(6, 10) + "-" + target.substring(3,5) + "-" + target.substring(0,2) + "T00:00:00.000Z"
            return target
        }

        fun formatDateToString(target: String): String {
            if(target.isNotEmpty())
                return target.substring(8,10) + "/" + target.substring(5,7) + "/" + target.substring(0,4)
            return target
        }

        fun Int.standardFormat(): String {
            return this.toLong().standardFormat()
        }

        fun Long.standardFormat(): String {
            return getDecimalFormat().format(this)
        }

        private fun getDecimalFormat(): DecimalFormat {
            val df = DecimalFormat("###,###.#")
            val decimalFormatSymbols = DecimalFormatSymbols()
            decimalFormatSymbols.decimalSeparator = '.'

            df.decimalFormatSymbols = decimalFormatSymbols
            df.roundingMode = RoundingMode.FLOOR
            return df
        }
    }

}