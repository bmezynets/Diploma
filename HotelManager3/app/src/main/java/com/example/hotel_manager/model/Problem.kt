package com.example.hotel_manager.model

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.IgnoreExtraProperties

private lateinit var database: DatabaseReference

@IgnoreExtraProperties
class Problem (
    problemID: Int,
    problemDescription: String,
    toRoom: Int?
        ){
}