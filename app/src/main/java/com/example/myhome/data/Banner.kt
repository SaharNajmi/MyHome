package com.example.myhome.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "banners")
@Parcelize
data class Banner(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val userID: Int,
    val title: String,
    val category: Int,
    val sellOrRent: Int,
    val description: String,
    val location: String,
    val homeSize: Int,
    val numberOfRooms: Int,
    val price: String,
    val bannerImage: String,
    val phone: String,
    val username: String,
    val userImage: String,
    val date: Int,
    val status: Int,
    var fav: Boolean
) : Parcelable
