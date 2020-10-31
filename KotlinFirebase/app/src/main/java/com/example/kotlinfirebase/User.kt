package com.example.kotlinfirebase

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class User(var userID: String,
                var name : String?,
                var surname:String?,
                var email:String?,
                var password:String?) {
constructor() : this("", "", "", "", "")
}