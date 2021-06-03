package com.example.arfashion.presentation.app.widget

import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import com.example.arfashion.presentation.app.presentation.login.LoginActivity

class ProgressBarAnimation: Animation {

    private var context: Context

    private var progressBar: ProgressBar

    private var from : Float

    private var to: Float

    constructor(context: Context, progressBar: ProgressBar, from: Float, to: Float){
        this.context = context
        this.progressBar = progressBar
        this.from = from
        this.to = to
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        var value: Float = from + (to- from) * interpolatedTime
        progressBar.setProgress(value.toInt())

        if(value == to){
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}