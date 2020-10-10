package com.devmanishpatole.imagesearcher.paging

import com.devmanishpatole.imagesearcher.base.BaseDataSource
import com.devmanishpatole.imagesearcher.data.ImageData
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.exception.NetworkException
import com.devmanishpatole.imagesearcher.service.ImgurService
import com.devmanishpatole.imagesearcher.util.NetworkHelper

class ImageSource(
    private val service: ImgurService,
    private val networkHelper: NetworkHelper,
    private val request: PhotoRequest
) : BaseDataSource<ImageData>() {

    override suspend fun loadFromLocalStorage(): List<ImageData>? {
        return null
    }

    override suspend fun loadFromNetwork(): List<ImageData>? {
        val results: List<ImageData>?

        if (networkHelper.isNetworkConnected()) {
            val response = if (request.query.isNotEmpty()) {
                service.searchImages(request.query, position)
            } else {
                service.getSectionImages(request.section.value, request.includeViral, position)
            }

            if (response.isSuccessful) {
                results = response.body()?.data
            } else {
                throw Exception()
            }
        } else {
            throw NetworkException()
        }
        return results
    }

    override fun getPreviousKey() =
        if (position == STARTING_PAGE_INDEX) null else position - 1

    override fun getNextKey(results: List<ImageData>) =
        if (results.isEmpty() || results.size < THRESHOLD) null else position + 1
}