package com.example.galleryapp.data.network

import com.example.galleryapp.data.model.PhotoItem
import io.reactivex.rxjava3.core.Flowable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("list")
    fun getPhotos(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Flowable<Response<List<PhotoItem>>>
}
