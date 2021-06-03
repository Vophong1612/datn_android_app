package com.example.arfashion.presentation.services
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.presentation.main.MainActivity

class ProgressBarAnimation: Animation {

    private var activity: Activity
    private var progressBar: ProgressBar
    private var from : Float
    private var to: Float

    companion object{
        var isDone: Boolean = true
        var value: Float = 0f
    }

    constructor(activity: Activity, progressBar: ProgressBar, from: Float, to: Float){
        this.activity = activity
        this.progressBar = progressBar
        this.from = from
        this.to = to
    }

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        super.applyTransformation(interpolatedTime, t)
        value = from + (to- from) * interpolatedTime
        progressBar.progress = value.toInt()
        if(value == to && isDone){
            isDone = false
            val intent = Intent(activity, LoginActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

}