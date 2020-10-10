package com.devmanishpatole.imagesearcher.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.devmanishpatole.imagesearcher.base.BaseViewModel
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.repository.GalleyRepository

class GalleryViewModel @ViewModelInject constructor(private val repository: GalleyRepository) :
    BaseViewModel() {

    private val currentQuery = MutableLiveData<PhotoRequest>()

    val photos = currentQuery.switchMap { request ->
        repository.getImages(PhotoRequest(request.query, request.section, request.includeViral))
            .cachedIn(viewModelScope)
    }

    fun searchImages(request: PhotoRequest) {
        currentQuery.value = request
    }

}