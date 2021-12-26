package com.example.kotlinfirebase

import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.concurrent.thread

class EmailVerificationService(var email:String?, var name: String?){
    var verificationCode: String  = ""
    fun SendEmailVerification():String{
        verificationCode = getRandomNumberString()
        thread {
            HTMLEmailSender.sending(
                "<div style='background-color=white width=100% height=100% text-allign=left'>" +
                        "<h3>Dear ${name!!.toUpperCase()}\"<h3><br></br>" +
                        "<p>Registration was successful. Last thing you need to do is confirm your" +
                        "<br>email by entering the code below<br>" +
                        " <h4 style='text-decoration=none' color:blue>${verificationCode}</h4></p></div>",
                email, "Thank you for registration"
            )
        }
        return verificationCode
    }
}

fun getRandomNumberString(): String {
    val rnd = Random()
    val number = rnd.nextInt(999999)
    return String.format("%06d", number)
}