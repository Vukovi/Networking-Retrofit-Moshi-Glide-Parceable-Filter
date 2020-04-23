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

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

private const val BASE_URL = "https://mars.udacity.com/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

// BEZ MOSHI FRAMEWORKA
// private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).baseUrl(BASE_URL).build()
// SA MOSHI FRAMEWORKOM
// private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create()).baseUrl(BASE_URL).build()
// SA MOSHI FRAMEWORKOM I COROUTINES
private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()


enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

interface MarsApiService {
    @GET("realestate")
    // BEZ MOSHI FRAMEWORKA
    // fun getProperties(): Call<String>
    // SA MOSHI FRAMEWORKOM
    // fun getProperties(): Call<List<MarsProperty>>
    // SA MOSHI FRAMEWORKOM i sa COROUTINES
//    fun getProperties(): Deferred<List<MarsProperty>>
    // SA MOSHI FRAMEWORKOM, COROUTINES i sa FILTEROM, koji ce biti naziv za Query, zavisi od backenda
    fun getProperties(@Query("filter") type: String): Deferred<List<MarsProperty>>
}

object MarsApi {
    val retrofitService : MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
