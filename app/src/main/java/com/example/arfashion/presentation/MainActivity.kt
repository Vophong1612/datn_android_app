package com.example.arfashion.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.arfashion.R
import com.example.arfashion.presentation.app.presentation.login.LoginActivity
import com.example.arfashion.presentation.app.widget.ProgressBarAnimation

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    companion object{
        var mode: String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        init()
    }

    fun init(){
        progressBar = findViewById(R.id.progress_bar_splash)
        progressBar.max = 100
        progressBar.scaleY = 3f

    }

    override fun onResume() {
        super.onResume()
        if(mode != "done") {
            progressAnimation()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }
    }

    private fun progressAnimation() {
        val anim = ProgressBarAnimation(this, progressBar, 0f, 100f)
        anim.duration = 2000
        progressBar.animation = anim

    }
}