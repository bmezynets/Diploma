package com.example.hotel_manager.model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

private lateinit var database: DatabaseReference
private var tableReservations = Firebase.database.reference.child("reservations")
private var tableUsers = Firebase.database.reference.child("users")

@IgnoreExtraProperties
class Reservation (
    reservationID: String,  //id rezerwacji
    dateFrom: Date,         //rezerwacja z
    dateTo: Date,           //rezerwacja do
    reservedBy: String,     //rezerwacja przez(user id)
    roomNumber: Int,        //nr pokoju
    description: String     //opis
        ){}

//metoda dodajaca rezerwacje do db
fun addReservation(reservationID: String, dateFrom: Date, dateTo: Date, reservedBy: String, roomNumber: Int, description: String): Boolean
{
    if(tableUsers.child(reservedBy).get().isSuccessful) {
        Log.i("firebase: ", "addReservation: returned false(No such user in db! Create user first.)")
        return false
    }

    var newReservation = Reservation(reservationID, dateFrom, dateTo, reservedBy, roomNumber, description)
    return tableReservations.child(reservationID).setValue(newReservation).isSuccessful
}

//metoda szuka rezerwacji w db przez podane id
fun findReservationById(id : String): Reservation?
{
    var reservation : Any? = null
    tableReservations.child(id).get().addOnSuccessListener {
        reservation = it.value
        Log.i("firebase: ", "getUserById: ${it.value}")
    }.addOnFailureListener{
        reservation = null
        Log.i("firebase err: ", "getUserById: $it")
    }

    if(reservation !is Reservation)
        return null
    return reservation as Reservation
}