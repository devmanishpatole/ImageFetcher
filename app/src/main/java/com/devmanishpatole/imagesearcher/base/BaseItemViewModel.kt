package com.devmanishpatole.imagesearcher.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseItemViewModel<T> : BaseViewModel() {

    private val _data = MutableLiveData<T>()
    val data: LiveData<T>
        get() = _data


    open fun updateData(data: T) {
        _data.postValue(data)
    }

    fun onManualClear() = onCleared()
}