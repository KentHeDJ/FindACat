package edu.gwu.findacat.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteCat(val image: String, val name: String, val sex: String, val description: String): Parcelable