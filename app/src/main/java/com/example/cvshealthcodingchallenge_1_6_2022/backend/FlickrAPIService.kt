package com.example.cvshealthcodingchallenge_1_6_2022.backend

import com.example.cvsheathcodechallenge.models.FlickrPhotoResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FlickrAPIService {

    @GET("photos_public.gne?format=json&nojsoncallback=1")
    suspend fun fetchImages(
        @Query("tag") queryString: String?
    ): Response<FlickrPhotoResponse>

    companion object {
        private val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
        var flickrAPIService: FlickrAPIService? = null
        fun getInstance() : FlickrAPIService? {
            if (flickrAPIService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.flickr.com/services/feeds/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(client)
                    .build()
                flickrAPIService = retrofit.create(FlickrAPIService::class.java)
            }
            return flickrAPIService
        }
    }
}