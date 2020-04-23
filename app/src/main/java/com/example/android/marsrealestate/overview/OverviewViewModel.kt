/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    // Download slika i MVVM sa retrofitom
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    // Event za navigaciju ka fragmentu detalja
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Metoda kojom se okida navigacije
    fun displayPropertydetails(marsProperty: MarsProperty){
        _navigateToSelectedProperty.value = marsProperty
    }

    // Metoda kojom se prekida event navigacije, kada se fragment ponovo vrati u stack
    fun displayPropertydetailsCompleted(){
        _navigateToSelectedProperty.value = null
    }

    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        // BEZ IKAKVIH FRAMEWOKRA
//        _response.value = "Set the Mars API Response here!"

        // BEZ MOSHI FRAMEWORKOM
//        MarsApi.retrofitService.getProperties().enqueue(object : Callback, retrofit2.Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _response.value = "Greska :" + t.message
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _response.value = response.body()
//            }
//
//        })

        // SA MOSHI FRAMEWORKOM
//        MarsApi.retrofitService.getProperties().enqueue(object : Callback, retrofit2.Callback<List<MarsProperty>> {
//            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//                _response.value = "Greska:" + t.message
//            }
//
//            override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//                _response.value = "Uspesno: ${response.body()?.size} dobijenih Mars Property-ja"
//            }
//
//        })

        // SA MOSHI FRAMEWORKOM i COROUTINES
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties(filter.value)
            try {
                _status.value = MarsApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                val listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                _properties.value = listResult
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    fun updateFilter(filter: MarsApiFilter){
        getMarsRealEstateProperties(filter)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
