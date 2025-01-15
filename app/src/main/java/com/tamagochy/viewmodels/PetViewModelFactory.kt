package com.tamagochy.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tamagochy.data.PetRepository
import com.tamagochy.data.UserRepository

class PetViewModelFactory (private val repository: PetRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(PetViewModel::class.java)) {
            PetViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }

    }
}