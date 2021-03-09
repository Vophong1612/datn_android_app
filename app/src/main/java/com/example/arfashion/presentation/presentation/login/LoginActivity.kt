package com.example.arfashion.presentation.presentation.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.arfashion.R
import kotlinx.android.synthetic.main.layout_back_header.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    private fun init() {
        screen_name.text = this.getString(R.string.sign_in)
    }

//    private fun setupAgreementText() {
//        val termsText = this.getString(R.string.terms_link)
//        val termsAgreement = this.getString(R.string.terms_of_use_agreement, termsText)
//        val start = termsAgreement.indexOf(termsText)
//        val end = start + termsText.length
//        val termsSpan = SpannableString(termsAgreement)
//        termsSpan.setSpan(
//            URLSpan(this.getString(R.string.terms_url)),
//            start,
//            end,
//            Spanned.SPAN_INCLUSIVE_INCLUSIVE
//        )
//        termsAgreementLabel.text = termsSpan
//        termsAgreementLabel.movementMethod = LinkMovementMethod.getInstance()
//    }
}