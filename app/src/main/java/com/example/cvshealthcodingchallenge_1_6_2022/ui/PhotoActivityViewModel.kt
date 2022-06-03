package com.example.cvshealthcodingchallenge_1_6_2022.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cvshealthcodingchallenge_1_6_2022.backend.Repository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Collections.emptyList

class PhotoActivityViewModel constructor(
    private val repository: Repository
) : ViewModel() {
    private val _mainViewState = MutableStateFlow<MainViewState>(MainViewState.LandingState)
    val mainViewStateFlow: StateFlow<MainViewState> = _mainViewState
    private val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun onAction(action: MainViewAction){
        when (action) {
            is MainViewAction.NavigateToDetailedView -> {
            }
            is MainViewAction.FetchPhotos -> {
                getFlickerPhotos(action.searchString)
            }
        }
    }

    private fun getFlickerPhotos(queryString: String?) {
        _mainViewState.value = MainViewState.LoadingState
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getPhotos(queryString)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Log.v("Success_Response", response.body().toString())
                    if (response.body()?.items?.isEmpty() == true){
                        _mainViewState.value = MainViewState.NoPhotosState
                    } else {
                        _mainViewState.value =
                            MainViewState.PhotosState(response.body()?.items ?: emptyList())
                    }
                } else {
                    onError("Error : ${response.message()} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        _mainViewState.value = MainViewState.ErrorState
        errorMessage.value = message
    }

    override fun onCleared() {
        super.onCleared()
        //job?.cancel()
    }
}