package com.example.kotlinfirebase

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class User(var userID: String,
                var name : String?,
                var surname:String?,
                var email:String?,
                var password:String?,
                var salt:String?,
                var userRole:String?,
                var isBanned:Boolean) :Serializable{
constructor() : this("", "", "", "", "", "","", false)
}