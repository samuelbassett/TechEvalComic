package com.tc.techevalcomic.data.remote

import javax.inject.Inject

class XkcdRepository @Inject constructor(
    private val api: XkcdApi
){
    suspend fun getComic(comicNumber: Int) = runCatching {
        api.getComic(comicNumber)
    }
}