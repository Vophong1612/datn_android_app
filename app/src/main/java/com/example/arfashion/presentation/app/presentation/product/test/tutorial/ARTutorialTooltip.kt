package com.example.arfashion.presentation.app.presentation.product.test.tutorial

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.arfashion.R

class ARTutorialToolTip(context: Context) : BaseToolTipView(
    context
), PopupWindow.OnDismissListener {

    private var dismissListener: PopupWindow.OnDismissListener? = null

    private lateinit var arrowUp: View

    private lateinit var arTutorialText: TextView

    init {
        createContentView(context)
        window.setOnDismissListener(this)
    }

    @SuppressLint("InflateParams")
    private fun createContentView(context: Context) {
        val rootView = LayoutInflater.from(context).inflate(R.layout.dialog_ar_test_tutorial, null)

        rootView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        arrowUp = rootView.findViewById(R.id.arrow_up)

        arTutorialText = rootView.findViewById(R.id.dailyMissionInfo)

        arTutorialText.movementMethod = ScrollingMovementMethod.getInstance()

        this.rootView = rootView
    }

    fun show(anchor: View) {
        rootView?.let { v ->
            preShow()

            v.measure(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )

            val location = IntArray(2)
            anchor.getLocationInWindow(location)

            val xPos = 0
            val yPos = location[1] + anchor.height
            val arrowPos = anchor.left - (arrowUp.measuredWidth / 2 - anchor.width / 2)

            val param = arrowUp.layoutParams as ViewGroup.MarginLayoutParams
            param.leftMargin = arrowPos

            showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos)
        }
    }

    override fun onDismiss() {
        dismissListener?.onDismiss()
    }
}