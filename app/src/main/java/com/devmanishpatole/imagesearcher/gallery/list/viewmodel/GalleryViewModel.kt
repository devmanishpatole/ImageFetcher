package com.devmanishpatole.imagesearcher.gallery.list.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.devmanishpatole.imagesearcher.base.BaseViewModel
import com.devmanishpatole.imagesearcher.gallery.list.repository.GalleyRepository
import com.devmanishpatole.imagesearcher.model.PhotoRequest

class GalleryViewModel @ViewModelInject constructor(
    private val repository: GalleyRepository,
    @Assisted state: SavedStateHandle
) : BaseViewModel() {

    private val currentQuery = state.getLiveData(RESTORED_REQUEST, PhotoRequest())

    val photos = currentQuery.distinctUntilChanged().switchMap { request ->
        repository.getImages(PhotoRequest(request.query, request.section, request.includeViral))
            .cachedIn(viewModelScope)
    }

    fun searchImages(request: PhotoRequest) {
        currentQuery.value = request
    }

    companion object {
        private const val RESTORED_REQUEST = "RESTORED_REQUEST"
    }

}