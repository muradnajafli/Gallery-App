package com.example.galleryapp.data.model


import com.google.gson.annotations.SerializedName

data class PhotoItem(
    @SerializedName("download_url")
    val downloadUrl: String?
)