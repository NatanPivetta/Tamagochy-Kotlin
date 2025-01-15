package com.tamagochy.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamagochy.data.PetRepository
import com.tamagochy.model.Pet
import kotlinx.coroutines.launch

class PetViewModel(private val repository: PetRepository) : ViewModel() {


    val pets = mutableStateOf<List<Pet>>(emptyList())
    val isLoading = mutableStateOf(true)
    val errorMessage = mutableStateOf<String?>(null)

    init {
        loadPets()
    }

    fun loadPets() {
        viewModelScope.launch {
            isLoading.value = false
            repository.getPetsForCurrentUser(
                onSuccess = { petList ->
                    pets.value = petList
                    isLoading.value = false
                },
                onFailure = { error ->
                    errorMessage.value = error
                    isLoading.value = false
                }
            )
        }
    }

    fun feedPet(pet: Pet) {
        repository.feedPet(
            pet,
            onSuccess = {
                loadPets() // Refresh pets after feeding
            },
            onFailure = { error ->
                errorMessage.value = error
            }
        )
    }
}
