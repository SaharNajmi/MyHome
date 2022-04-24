package com.example.myhome.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Int,
    val image: String,
    val password: String,
    val username: String,
    val phone: String
) : Parcelable
