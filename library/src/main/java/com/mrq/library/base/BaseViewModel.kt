package com.mrq.library.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<T> : ViewModel() {

    val exception = MutableLiveData<String>()
    val offline = MutableLiveData<String>()
    val loading = MutableLiveData<Boolean>()
    val isEmpty = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()
    val success = MutableLiveData<T>()

}