package com.devmanishpatole.imagesearcher.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.paging.ImageSource
import com.devmanishpatole.imagesearcher.service.ImgurService
import com.devmanishpatole.imagesearcher.util.NetworkHelper
import javax.inject.Inject

class GalleyRepository @Inject constructor(
    private val service: ImgurService,
    private val networkHelper: NetworkHelper
) {

    fun getImages(request: PhotoRequest) = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            ImageSource(
                service,
                networkHelper,
                request
            )
        }
    ).liveData

    companion object {
        private const val PAGE_SIZE = 60
    }
}