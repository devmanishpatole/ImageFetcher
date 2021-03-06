package com.devmanishpatole.imagesearcher.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PhotoRequest(
    val query: String = "",
    val section: Section = Section.UNKNOWN,
    val includeViral: Boolean = false
) : Parcelable