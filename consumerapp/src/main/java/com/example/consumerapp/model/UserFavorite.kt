package com.example.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserFavorite (
    var id: Int = 0,
    var avatar: String? = null,
    var username: String? = null,
    var url: String? = null
): Parcelable