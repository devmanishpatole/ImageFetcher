package com.devmanishpatole.imagesearcher.gallery.list.paging

import com.devmanishpatole.imagesearcher.base.BaseDataSource
import com.devmanishpatole.imagesearcher.exception.NetworkException
import com.devmanishpatole.imagesearcher.gallery.list.service.ImgurService
import com.devmanishpatole.imagesearcher.model.ImageData
import com.devmanishpatole.imagesearcher.model.ImageWrapper
import com.devmanishpatole.imagesearcher.model.PhotoRequest
import com.devmanishpatole.imagesearcher.util.NetworkHelper
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class ImageSource(
    private val service: ImgurService,
    private val networkHelper: NetworkHelper,
    private val request: PhotoRequest
) : BaseDataSource<ImageData>() {

    override suspend fun loadFromLocalStorage(): List<ImageData>? {
        // In case local cache implemented
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

            if (isSuccessful(response)) {
                results = response.body()?.data
            } else {
                throw Exception()
            }
        } else {
            throw NetworkException()
        }
        return results
    }

    private fun isSuccessful(response: Response<ImageWrapper>): Boolean {
        var isSuccess = false
        if (response.isSuccessful) {
            val result = response.body()
            result?.let {
                if (it.success && it.status == HttpsURLConnection.HTTP_OK) {
                    isSuccess = true
                }
            }
        }
        return isSuccess
    }

    override fun getPreviousKey() =
        if (position == STARTING_PAGE_INDEX) null else position - 1

    override fun getNextKey(results: List<ImageData>) =
        if (results.isEmpty() || results.size < THRESHOLD) null else position + 1
}