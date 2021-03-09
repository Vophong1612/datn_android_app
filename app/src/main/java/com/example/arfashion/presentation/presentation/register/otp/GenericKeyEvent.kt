package com.example.arfashion.presentation.presentation.register.otp

import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import com.example.arfashion.R

class GenericKeyEvent internal constructor(
    private val view: EditText,
    private val previousView: EditText?
) : View.OnKeyListener {
    override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL && view.id != R.id.first && view.text.isEmpty()) {
            previousView?.text?.clear()
            previousView?.requestFocus()
            return true
        }
        return false
    }
}