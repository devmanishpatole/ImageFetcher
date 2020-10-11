package com.devmanishpatole.imagesearcher.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmanishpatole.imagesearcher.base.BaseViewModel
import com.devmanishpatole.imagesearcher.data.ViralSelection

class MainViewModel @ViewModelInject constructor() : BaseViewModel() {

    private val _showCategories = MutableLiveData<Boolean>()
    val showCategories: LiveData<Boolean> = _showCategories

    private val _setAppBarExpanded = MutableLiveData<Boolean>()
    val setAppBarExpanded: LiveData<Boolean> = _setAppBarExpanded

    private val _viral = MutableLiveData<ViralSelection>()
    val viral: LiveData<ViralSelection> = _viral

    private val _resetViral = MutableLiveData<Unit>()
    val resetViral: LiveData<Unit> = _resetViral

    fun showCategories(show: Boolean) = _showCategories.postValue(show)

    fun setViral(viral: ViralSelection) = _viral.postValue(viral)

    fun setAppBarExpanded(expanded: Boolean) = _setAppBarExpanded.postValue(expanded)

    fun resetViralSelection() {
        _resetViral.postValue(Unit)
    }
}