package com.example.kotlinfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_admin_users_service.*

class AdminUsersService : AppCompatActivity() {

    private var databaseRvareference: DatabaseReference? = null
    private var userList = mutableListOf<User?>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_users_service)


        searc_user_field.setOnClickListener{
            firebaseUserSearch()
        }

    }

    class ListViewAdapter() : RecyclerView.Adapter(){
        override fun onCreateView(parent:ViewGroup?, viewType:Int):UsersViewHolder{
            val textView = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.list_layout, parent, false) as TextView
            return UsersViewHolder(textView)
        }
        override fun onBindViewHolder(holder: UsersViewHolder?, position: Int) {

        }

        override fun getItemCount(): Int{}
        class UsersViewHolder( itemView: View): RecyclerView.ViewHolder(itemView){}
    }



        //override fun onCreateViewHolder(parent: ViewGroup, ViewType: Int):

    fun findUsers(searchString: String): MutableList<User?>{
        var findUsersList = mutableListOf<User?>()
        databaseRvareference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    userList.add(it.getValue(User::class.java))
                }
                userList.forEach{
                    when{
                        it!!.name!!.contains(searchString) ||
                        it!!.surname!!.contains(searchString) ||
                        it!!.email!!.contains(searchString) -> findUsersList.add(it)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ERROR IN READING DATABASE", "findUsers:onCancelled", databaseError!!.toException())
                Toast.makeText(this@AdminUsersService, "Failed to load Message.", Toast.LENGTH_SHORT).show()
            }
        })

        return findUsersList
    }
    fun firebaseUserSearch(){

    }
}
