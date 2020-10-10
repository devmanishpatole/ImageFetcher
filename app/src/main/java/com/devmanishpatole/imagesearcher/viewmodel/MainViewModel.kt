package com.devmanishpatole.imagesearcher.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmanishpatole.imagesearcher.base.BaseViewModel
import com.devmanishpatole.imagesearcher.data.Section

class MainViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val _showCategories = MutableLiveData<Boolean>()
    val showCategories: LiveData<Boolean> = _showCategories

    private val _viral = MutableLiveData<Boolean>()
    val viral: LiveData<Boolean> = _viral

    private val _section = MutableLiveData<Section>()
    val section: LiveData<Section> = _section

    fun showCategories(show: Boolean) = _showCategories.postValue(show)

    fun setupSection(section: Section) = _section.postValue(section)

    fun setViral(viral: Boolean) = _viral.postValue(viral)
}