package com.devmanishpatole.imagesearcher.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.devmanishpatole.imagesearcher.base.BaseViewModel
import com.devmanishpatole.imagesearcher.data.PhotoRequest
import com.devmanishpatole.imagesearcher.data.Section
import com.devmanishpatole.imagesearcher.repository.GalleyRepository

class GalleryViewModel @ViewModelInject constructor(private val repository: GalleyRepository) :
    BaseViewModel() {

    fun searchImages(query: String, section: Section, includeViral: Boolean) =
        repository.getImages(PhotoRequest(query, section, includeViral)).cachedIn(viewModelScope)

}