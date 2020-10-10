package com.devmanishpatole.imagesearcher.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.devmanishpatole.imagesearcher.paging.ImageSource
import com.devmanishpatole.imagesearcher.data.ImageData
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.service.ImgurService
import com.devmanishpatole.imagesearcher.util.NetworkHelper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GalleyRepository @Inject constructor(
    private val service: ImgurService,
    private val networkHelper: NetworkHelper
) {

    fun getImages(request: PhotoRequest): Flow<PagingData<ImageData>> = Pager(
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
    ).flow

    companion object {
        private const val PAGE_SIZE = 60
    }
}