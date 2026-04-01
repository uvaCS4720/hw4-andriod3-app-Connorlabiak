package edu.nd.pmcburne.hello

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Requester {
    val api: APIInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.cs.virginia.edu/~wxt4gm/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIInterface::class.java)
    }
}