package com.example.konturtest.api

import android.content.Context
import android.net.ConnectivityManager
import com.example.konturtest.db.model.Contact
import io.reactivex.Observable
import okhttp3.Cache
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
        fun create(context: Context): ContactsService {
            // 10 MB for cache
            val cacheSize = (10 * 1024 * 1024).toLong()
            val myCache = Cache(context.cacheDir, cacheSize)

            /*
            * If network is active then set a lifetime of cache to 60 seconds
            * or take data from a local store
            */
            val okHttpClient = OkHttpClient.Builder()
                .cache(myCache)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (hasNetwork(context)) {
                        request.newBuilder().header("Cache-Control", "public, max-age=" + 60)
                            .build()
                    } else {
                        request.newBuilder().header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                        ).build()
                    }
                    chain.proceed(request)
                }
                .build()
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/")
                .build()
                .create(ContactsService::class.java)

        }

        // Network checking fun
        private fun hasNetwork(context: Context): Boolean {
            var isConnected = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                isConnected = true
            }
            return isConnected
        }
    }
}