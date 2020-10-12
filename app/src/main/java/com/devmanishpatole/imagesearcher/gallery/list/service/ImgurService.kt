package com.devmanishpatole.imagesearcher.gallery.list.service

import com.devmanishpatole.imagesearcher.model.ImageWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImgurService {

    @GET("3/gallery/search")
    suspend fun searchImages(
        @Query("q") search: String,
        @Query("page") page: Int
    ): Response<ImageWrapper>

    @GET("3/gallery/{section}")
    suspend fun getSectionImages(
        @Path("section") section: String,
        @Query("showViral") showViral: Boolean = true,
        @Query("page") page: Int
    ): Response<ImageWrapper>
}