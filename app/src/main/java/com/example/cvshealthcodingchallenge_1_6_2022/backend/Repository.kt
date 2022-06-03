package com.example.cvshealthcodingchallenge_1_6_2022.backend

class Repository constructor(private val retrofitService: FlickrAPIService) {
    suspend fun getPhotos(queryString: String?) = retrofitService.fetchImages(queryString)
}
