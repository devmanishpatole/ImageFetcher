package com.devmanishpatole.imagesearcher.gallery.list.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.devmanishpatole.imagesearcher.gallery.list.paging.ImageSource
import com.devmanishpatole.imagesearcher.gallery.list.service.ImgurService
import com.devmanishpatole.imagesearcher.model.PhotoRequest
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