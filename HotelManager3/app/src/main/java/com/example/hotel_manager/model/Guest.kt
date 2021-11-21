package com.example.hotel_manager.model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.io.Serializable

private var dbReference: DatabaseReference? = null
private var tableGuests = Firebase.database.reference.child("guests")
private var tableUsers = Firebase.database.reference.child("users")
private var tableReservations = Firebase.database.reference.child("reservations")

@IgnoreExtraProperties
class Guest (
            guestID : String,   //id goscia
            userID: String,     //id usera
            reservationID: Int, //id rezerwacji
            problemID: Int?,    //id usterki
            imgID: Int          //id zdjecia
                ) :Serializable

//metoda dodaje goscia do db
fun addGuest(guestID: String, userID: String, reservationID: Int, problemID: Int?, imgID: Int): Boolean
{
    if(tableUsers.child(userID).get().isSuccessful) {
        Log.i("firebase: ", "addGuest: returned false(No such user in db! Create user first.)")
        return false
    }

    var newGuest = Guest(guestID, userID, reservationID, problemID = null, imgID)
    return tableGuests.child(guestID).setValue(newGuest).isSuccessful
}

//metoda szuka goscia w db przez podane id
fun findGuestById(id : String): Guest?
{
    var guest : Any? = null
    tableGuests.child(id).get().addOnSuccessListener {
        guest = it.value
        Log.i("firebase: ", "getUserById: ${it.value}")
    }.addOnFailureListener{
        guest = null
        Log.i("firebase err: ", "getUserById: $it")
    }

    if(guest !is Guest)
        return null
    return guest as Guest
}

//metoda szuka goscia w db przez podane id rezerwacji
fun findGuestByReservationId(id : String): Guest?
{
    var guest : Any? = null
    tableReservations.child(id).get().addOnSuccessListener {
        guest = it.value
        Log.i("firebase: ", "findGuestByReservationId: ${it.value}")
    }.addOnFailureListener{
        guest = null
        Log.i("firebase err: ", "findGuestByReservationId: $it")
    }

    if(guest !is Guest)
        return null
    return guest as Guest
}
