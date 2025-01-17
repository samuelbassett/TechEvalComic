package com.tc.techevalcomic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tc.techevalcomic.data.remote.XkcdComicResponse
import com.tc.techevalcomic.data.remote.XkcdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class XkcdViewModel @Inject constructor(
    private val repository: XkcdRepository
): ViewModel() {
    private val _comics = MutableStateFlow<List<XkcdComicResponse>>(emptyList())
    val comics: StateFlow<List<XkcdComicResponse>> = _comics

    private val _selectedComic = MutableStateFlow<XkcdComicResponse?>(null)
    val selectedComic: StateFlow<XkcdComicResponse?> = _selectedComic

    fun fetchComic(comicNumber: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getComic(comicNumber)
                result.onSuccess { comic ->
                    _comics.value += comic
                    println("Fetched comic: $comicNumber - ${comic.title}")
                }.onFailure { throwable ->
                    println("Error fetching comic $comicNumber: ${throwable.message}")
                }
            } catch (e: Exception) {
                println("Error fetching comic $comicNumber: ${e.message}")
            }
        }
    }

    fun selectComic(comic: XkcdComicResponse) {
        _selectedComic.value = comic
    }
}