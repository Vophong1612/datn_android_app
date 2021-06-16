package com.example.arfashion.presentation.services

import android.text.TextUtils
import android.util.Patterns

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
    }

}