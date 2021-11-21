package com.example.hotel_manager.model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

private var dbReference: DatabaseReference? = null
private var tableUsers = Firebase.database.reference.child("users")

@IgnoreExtraProperties
data class User(var userID: String,     //id uzytkownika
                var name : String?,     //imie
                var surname:String?,    //nazwisko
                var email:String?,      //e-mail
                var password:String?,   //haslo
                var salt:String?,       //sol
                var userRole:String?,   //rol usera(admin, uzytkownuk zalogowany/nie)
                var isBanned:Boolean?   //czy zablokowany
                     ) :Serializable{}

fun addUser(id: String, name: String?, surname: String?,
            email: String?, password: String?, salt: String?,
            userRole: String?, isBanned: Boolean?): Boolean//metoda dodajaca uzytkownika do db
{
    var newUser = User(id, name, surname, email, password, salt, userRole, isBanned)
    return tableUsers.child(id).setValue(newUser).isSuccessful
}

fun findUserById(id : String): User?//metoda szuka uzytkownika w db rzez id
{
    var user : Any? = null
    tableUsers.child(id).get().addOnSuccessListener {
         user = it.value
         Log.i("firebase: ", "getUserById: ${it.value}")
    }.addOnFailureListener{
         user = null
         Log.i("firebase err: ", "getUserById: $it")
    }

    if(user !is User)
        return null
   return user as User
}

fun findUserByEmail(email: String) : User?//metoda szuka uzytkownika w db rzez e-mail
{
    var user : Any? = null
    tableUsers.child("users").get().addOnSuccessListener {
        user = it.value
        Log.i("firebase: ", "getUserByEmail: ${it.value}")
    }.addOnFailureListener{
        user = null
        Log.i("firebase err: ", "getUserByEmail: $it")
    }

    if(user !is User)
        return null
    return user as User
}