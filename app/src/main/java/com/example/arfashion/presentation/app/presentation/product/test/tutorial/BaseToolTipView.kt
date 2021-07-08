package com.example.arfashion.presentation.app.presentation.product.test.tutorial

import android.content.Context
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow

open class BaseToolTipView(context: Context, private val delay: Long = 0) {

    val window: PopupWindow = PopupWindow(context)

    private var handler: Handler? = null

    private val closeRunnable = Runnable {
        dismiss()
    }

    var rootView: View? = null
        set(value) {
            field = value
            window.contentView = field
        }

    init {
        window.setTouchInterceptor { view: View, event: MotionEvent ->
            when (event.action) {
                MotionEvent.ACTION_OUTSIDE -> {
                    window.dismiss()
                    true
                }
                MotionEvent.ACTION_BUTTON_PRESS -> {
                    view.performClick()
                    true
                }
                else -> false
            }
        }
    }

    protected fun preShow(){
        window.width = WindowManager.LayoutParams.WRAP_CONTENT
        window.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.isTouchable = true
        window.isFocusable = true
        window.isOutsideTouchable = true
        window.contentView = rootView
        window.setBackgroundDrawable(null)
    }

    protected fun setSize(width: Int, height: Int) {
        window.width = width
        window.height = height
    }

    fun showAtLocation(anchor: View, gravity: Int, xPos: Int, yPos: Int){
        if (delay > 0){
            if (handler == null){
                handler = Handler()
            }
            handler?.removeCallbacks(closeRunnable)
            handler?.postDelayed(closeRunnable, delay)
        }

        window.showAtLocation(anchor, gravity, xPos, yPos)
    }

    fun dismiss() {
        handler?.removeCallbacks(closeRunnable)
        window.dismiss()
    }
}