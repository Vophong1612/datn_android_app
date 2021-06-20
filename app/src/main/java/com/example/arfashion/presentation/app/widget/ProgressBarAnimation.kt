package com.example.arfashion.presentation.app.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import com.example.arfashion.presentation.MainActivity
import com.example.arfashion.presentation.app.presentation.login.LoginActivity

class ProgressBarAnimation: Animation {

    private var context: Activity

    private var progressBar: ProgressBar

    private var from : Float

    private var to: Float

    constructor(context: Activity, progressBar: ProgressBar, from: Float, to: Float){
        this.context = context
        this.progressBar = progressBar
        this.from = from
        this.to = to
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        val value: Float = from + (to- from) * interpolatedTime
        progressBar.progress = value.toInt()

        if(value == to){
            MainActivity.mode = "done"
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            context.finish()
        }
    }
}