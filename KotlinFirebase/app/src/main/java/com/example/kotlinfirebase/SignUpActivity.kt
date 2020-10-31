package com.example.kotlinfirebase

import android.app.ProgressDialog
import android.content.Context
import  android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Config
import android.util.Log
import android.util.Patterns
import android.widget.Toast
//import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.concurrent.thread

class SignUpActivity : AppCompatActivity() {

    // private lateinit var auth : FirebaseAuth
    private val TAG = "TAG FOR EXCEPTION"
    private var database: DatabaseReference? = null
    private var userReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        database = FirebaseDatabase.getInstance().reference
        userReference = FirebaseDatabase.getInstance().getReference("user")
        btnRegister.setOnClickListener {
            writeNewUser(UUID.randomUUID().toString())
        }

    }

    private fun writeNewUser(userId: String) {
        if (inputFirstName.text.isEmpty()) {
            inputFirstName.error = "Please enter name"
            inputFirstName.requestFocus()
            return
        } else if (inputSecondName.text.isEmpty()) {
            inputSecondName.error = "Please enter name"
            inputSecondName.requestFocus()
            return
        } else if (inputEmail.text.isEmpty()) {
            inputEmail.error = "Please enter name"
            inputEmail.requestFocus()
            return
        } else if (inputPassword.text.isEmpty()) {
            inputPassword.error = "Please enter name"
            inputPassword.requestFocus()
            return
        } else if (repeatPassword.text.toString().equals(inputPassword.text.toString()) == false) {
            repeatPassword.error = "Passwords doesn't match"
            repeatPassword.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.text.toString()).matches() == true) {
            inputEmail.error = "Please enter valid email"
            inputEmail.requestFocus()
            return
        }
        val user = User(
            userId, inputFirstName.text.toString(),
            inputSecondName.text.toString(), inputEmail.text.toString(),
            inputPassword.text.toString()
        )
        database!!.child("user").child(userId).setValue(user).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                thread{
                try {
                    var sender: GMailSender = GMailSender(
                        "mznbogdan@gmail.com", "bogdan2000"
                    )
                    sender.sendMail(
                        "Thank you for registration",
                        "Dear ${inputFirstName.text.toString().toUpperCase()}" +
                                "\nRegistration was successful. Last thing you need to do is confirm your" +
                                "email by clicking the link below\n <https:://somelink.com/confirmation>",
                        "mznbogdan@gmail.com", inputEmail.text.toString()
                    )
                }catch (e: Exception){
                  Log.w(TAG, e.message)
                }}
                Toast.makeText(this, "Registration was successful", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this, MainApplicationActivity::class.java).putExtra(
                        "name",
                        inputFirstName.text.toString()
                    )
                )

            } else {
                Log.w(TAG, task.exception)
                Toast.makeText(this, "Something went wrong. Try again later", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}


