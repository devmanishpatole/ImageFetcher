package com.devmanishpatole.imagesearcher.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageData(
    val datetime: Int,
    val id: String,
    val link: String,
    val title: String,
    val description: String?,
    val ups: Int,
    val downs: Int,
    val score: Int,
    @SerializedName("in_most_viral") val isViral: Boolean,
    val images: List<Image>?
) : Parcelable


@Parcelize
data class Image(
    @SerializedName("id")
    val imageId: String,
    @SerializedName("link")
    val imageLink: String
) : Parcelable