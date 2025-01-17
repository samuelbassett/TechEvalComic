package com.tc.techevalcomic.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface XkcdApi {
    @GET("{comicNumber}/info.0.json")
    suspend fun getComic(@Path("comicNumber") comicNumber: Int): XkcdComicResponse
}

data class XkcdComicResponse(
    val month: String,
    val year: String,
    val img: String,
    val title: String
)