package com.chiki.marsrealestate.detail

import android.app.Application
import androidx.lifecycle.*
import com.chiki.marsrealestate.network.MarsProperty

class DetailViewModel(selectedMarsProperty: MarsProperty): ViewModel(){

    //States
    private val _marsProperty = MutableLiveData<MarsProperty>()
    val marsProperty:LiveData<MarsProperty> get() = _marsProperty       //Current Property

    //Lifecycle
    init {
        _marsProperty.value = selectedMarsProperty
    }
}

class DetailViewModelFactory(private val marsProperty: MarsProperty) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(marsProperty) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}