package com.tamagochy.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tamagochy.data.PetRepository
import com.tamagochy.model.Pet

class PetViewModel(private val repository: PetRepository) : ViewModel() {

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> get() = _pets

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadPets() {
        repository.getPetsForCurrentUser(
            onSuccess = { petList ->
                _pets.postValue(petList)
            },
            onFailure = { errorMessage ->
                _error.postValue(errorMessage)
            }
        )
    }

    fun feedPet(pet: Pet) {
        repository.feedPet(
            pet,
            onSuccess = {
                loadPets() // Refresh pets after feeding
            },
            onFailure = { errorMessage ->
                _error.postValue(errorMessage)
            }
        )
    }
}
