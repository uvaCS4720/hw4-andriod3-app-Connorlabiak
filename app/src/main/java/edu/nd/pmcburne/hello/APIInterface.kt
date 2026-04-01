package edu.nd.pmcburne.hello

import edu.nd.pmcburne.hello.APIResponseObjects.APIResponse
import retrofit2.http.GET

interface APIInterface {
    @GET("placemarks.json")
    suspend fun getPlacemarks(): APIResponse
}