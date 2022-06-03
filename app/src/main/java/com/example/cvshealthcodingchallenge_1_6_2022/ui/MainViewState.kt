package com.example.cvshealthcodingchallenge_1_6_2022.ui

import android.icu.text.CaseMap
import com.example.cvsheathcodechallenge.models.Items

sealed class MainViewState  {
    object LandingState: MainViewState()
    object LoadingState : MainViewState()
    object NoPhotosState : MainViewState()
    data class PhotosState(
        val photos: List<Items>
    ) : MainViewState()
    object ErrorState: MainViewState()
}

sealed class MainViewAction {
    data class NavigateToDetailedView(
        val imageUrl: String?,
        val title: String?,
        val description: String?,
        val author: String?
    ) : MainViewAction()
    data class FetchPhotos(
        val searchString: String
    ) : MainViewAction()
}