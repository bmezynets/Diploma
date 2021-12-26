package com.example.kotlinfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class SignInActivity : AppCompatActivity() {
    private  var database : DatabaseReference? = null
    lateinit var userReference : DatabaseReference

    lateinit var userList: MutableList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance().reference
        userReference = FirebaseDatabase.getInstance().getReference("user")
        userList = mutableListOf()

        go.setOnClickListener{
           startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        btnLogin.setOnClickListener{
            userReference.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0!!.exists()){
                        userList.clear()
                        for (u in p0.children){
                            val user = u.getValue(User::class.java)
                            userList.add(user!!)
                        }
                        var u: User? = userList.find {
                            it.email == inputEmail.text.toString() &&
                                   it.password!!.equals(PasswordUtil.hashPassword(inputPassword.text.toString(), it.salt!!.toString()))
                        }
                        if(u == null){
                            Toast.makeText(applicationContext, "Login or password is wrong. Check and try again.", Toast.LENGTH_SHORT).show()
                            inputEmail.requestFocus()
                            inputPassword.requestFocus()
                        }else if(u.userRole == "Admin"){
                            startActivity(
                                Intent(this@SignInActivity, UserManagementActivity::class.java)
                                    .putExtra("name", u!!.name)
                            )
                        }
                        else{
                            startActivity(Intent(applicationContext, MainApplicationActivity::class.java).putExtra("name", u!!.name))
                        }
                    }
                }

            })
        }

    }


}
