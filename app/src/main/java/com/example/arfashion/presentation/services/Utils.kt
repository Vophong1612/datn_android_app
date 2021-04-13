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
    }

}