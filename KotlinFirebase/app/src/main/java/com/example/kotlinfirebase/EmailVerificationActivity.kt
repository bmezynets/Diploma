package com.example.kotlinfirebase

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_email_verification.*
import java.util.*

class EmailVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        var vrCode: String = intent.getStringExtra("code")
        var user = intent.getSerializableExtra("user") as User
        var attempt = 3
        btnContinue.setOnClickListener {
            if (inputVerificationCode.text.toString() == vrCode) {
                val returnIntent = Intent()
                returnIntent.putExtra("user", user)
                returnIntent.putExtra("ok", true)
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
            while(inputVerificationCode.text.toString() != vrCode && attempt > 0) {
                    inputVerificationCode.error = "Error code! Resend and try again."
                    inputVerificationCode.requestFocus()
                if(attempt-- == 0)
                    finish()
            }
        }

        resendEmail.setOnClickListener{
                var email = EmailVerificationService(user.email, user.name)
                vrCode = email.SendEmailVerification()
            }
        }
    }





