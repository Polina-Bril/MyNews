package com.itransition.api

import com.itransition.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//Retrofit Singleton
class RetrofitInstance {
    companion object {
        //lazy means that we will initialize it just once
        private val retrofit by lazy {
            //log responses of Retrofit to see which request we already make and the response
            val logging = HttpLoggingInterceptor()
            //we will see the body of Response(the actual response)
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            //creating a network client
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            //building retrofit and convert it to Kotlin object
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build()
        }
        //getting api object from retrofit. We can use it everywhere to make a network request
        val api by lazy {
            retrofit.create(NewsAPI::class.java)
        }
    }
}