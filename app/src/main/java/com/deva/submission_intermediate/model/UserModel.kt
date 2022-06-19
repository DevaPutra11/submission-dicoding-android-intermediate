package com.deva.submission_intermediate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id:String,
    val name:String,
    val description:String,
    val photoUrl:String,
    val createdAt:String,
    val lat:String,
    val lon:String
) : Parcelable
