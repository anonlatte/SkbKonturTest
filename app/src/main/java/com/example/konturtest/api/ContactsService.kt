package com.example.konturtest.api

import com.example.konturtest.db.model.Contact
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ContactsService {

    // example: generated-01.json
    @GET("{fileName}")
    fun getContacts(@Path("fileName") authorization: String): Observable<List<Contact>>

    companion object {
        fun create(): ContactsService {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
                .build()
                .create(ContactsService::class.java)

        }
    }
}