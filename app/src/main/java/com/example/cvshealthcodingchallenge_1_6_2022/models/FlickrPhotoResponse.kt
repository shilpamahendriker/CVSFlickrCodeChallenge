package com.example.cvsheathcodechallenge.models

import com.squareup.moshi.Json

data class FlickrPhotoResponse(

    @Json(name = "title") var title: String,
    @Json(name = "link") var link: String,
    @Json(name = "description") var description: String,
    @Json(name = "modified") var modified: String,
    @Json(name = "generator") var generator: String,
    @Json(name = "items") var items: List<Items>

)

data class Media(
    @Json(name = "m") var m: String
)

data class Items(

    @Json(name = "title") var title: String,
    @Json(name = "link") var link: String,
    @Json(name = "media") var media: Media,
    @Json(name = "date_taken") var dateTaken: String,
    @Json(name = "description") var description: String,
    @Json(name = "published") var published: String,
    @Json(name = "author") var author: String,
    @Json(name = "author_id") var authorId: String,
    @Json(name = "tags") var tags: String

)
