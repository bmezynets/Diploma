package com.example.kotlinfirebase

//import com.google.firebase.auth.FirebaseAuth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var userRole = listOf("Admin", "User", "Employee")
    private var verificationCode: Int = 1

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
        var pwdSalt = PasswordUtil.generateSalt(512).get()
        var key: String? = PasswordUtil.hashPassword(inputPassword.text.toString(), pwdSalt).toString()
        val user = User(
            userId, inputFirstName.text.toString(),
            inputSecondName.text.toString(),
            inputEmail.text.toString(), key, pwdSalt,
            checkUserRole(inputEmail.text.toString()),
            false
        )
        addUserAndSendVerivication(user)
    }

    private fun checkUserRole(email: String): String{
        when(email){
            "mznbogdan@gmail.com" -> return userRole.get(0)
            else -> return userRole.get(1)
        }
    }

    private fun addUserAndSendVerivication(u: User){
        var mailService = EmailVerificationService(inputEmail.text.toString(), inputFirstName.text.toString())
        var intent =  Intent(this, EmailVerificationActivity::class.java)
        /*intent.putExtra("name", inputFirstName.text.toString())
        intent.putExtra("email", inputEmail.text.toString())*/
        intent.putExtra("user", u)
        intent.putExtra("code",  mailService.SendEmailVerification())
        startActivityForResult(intent, 12)

    }

    fun saveUserInDatabase(u:User){
        database!!.child("user").child(u.userID).setValue(u).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Registration was successful", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this, MainApplicationActivity::class.java)
                        .putExtra("name", inputFirstName.text.toString())
                )
            } else {
                Log.w(TAG, task.exception)
                Toast.makeText(this, "Something went wrong. Try again later", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 12){
            if(data!!.getBooleanExtra("ok", false)){
                saveUserInDatabase(data!!.getSerializableExtra("user") as User)
            }else{
                Toast.makeText(this, "Unexpected Error Occurred!", Toast.LENGTH_SHORT)
            }
        }
    }

}


