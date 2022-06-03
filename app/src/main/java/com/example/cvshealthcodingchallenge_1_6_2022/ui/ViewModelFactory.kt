package com.example.cvshealthcodingchallenge_1_6_2022.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cvshealthcodingchallenge_1_6_2022.backend.Repository

class ViewModelFactory constructor(private val repository: Repository): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(PhotoActivityViewModel::class.java)) {
                PhotoActivityViewModel(this.repository) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
