package com.chiki.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chiki.marsrealestate.network.MarsApi
import com.chiki.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class MarsApiStatus{ LOADING, ERROR, DONE }
enum class MarsApiFilter{ ALL, RENT, BUY }
class OverViewViewModel:ViewModel() {

    //Coroutines
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)        //To do the background work

    //Events
    private val _selectedMarsProperty = MutableLiveData<MarsProperty>()     //When user selects a Property it is used to navigate to detail fragment
    val selectedMarsProperty:LiveData<MarsProperty> get() = _selectedMarsProperty

    //States
    private val _status = MutableLiveData<MarsApiStatus>()               //Response from network
    val status:LiveData<MarsApiStatus> get() = _status
    private val _properties = MediatorLiveData<List<MarsProperty>>()        //Properties to show
    val properties:LiveData<List<MarsProperty>> get() = _properties

    //Lifecycle
    init {
        updateFilter(MarsApiFilter.ALL)     //First lets get all properties in server
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()   //When viewModel gets destroyed cancel all background work
    }

    //Buttons
    fun onSelectMarsProperty(marsProperty: MarsProperty){
        _selectedMarsProperty.value = marsProperty      //When user selects a property
    }

    //Actions
    private fun getMarsRealEstateProperties(value:String){
        coroutineScope.launch {
            val getPropertiesDeferred =  MarsApi.retrofitService.getProperties(value)
            try {
                _status.value = MarsApiStatus.LOADING
                val listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                if (listResult.isNotEmpty())
                    _properties.value = listResult
            }catch (t: Throwable){
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }       //Gets the properties with the filter
    fun updateFilter(filter:MarsApiFilter){
        val value = when(filter){
            MarsApiFilter.ALL-> "all"
            MarsApiFilter.RENT-> "rent"
            MarsApiFilter.BUY-> "buy"
            else-> "all"
        }
        getMarsRealEstateProperties(value)
    }       //Change the filter
    fun doneNavigateToDetailFragment(){
        _selectedMarsProperty.value = null
    }   //Navigation is done
}