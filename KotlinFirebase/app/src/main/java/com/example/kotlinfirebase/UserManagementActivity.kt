package com.example.kotlinfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_user_management.*

class UserManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        dashboardTextView.text = "Hi, " + intent.getStringExtra("name")
        guests.setOnClickListener{
            startActivity(Intent(this, AdminUsersService::class.java))
        }
    }
}
