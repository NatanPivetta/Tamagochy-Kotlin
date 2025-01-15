package com.tamagochy.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tamagochy.data.PetRepository
import com.tamagochy.data.UserRepository
import com.tamagochy.model.Pet

class MainViewModel(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    private val _isUserLoggedIn = mutableStateOf(false)
    val isUserLoggedIn: State<Boolean> = _isUserLoggedIn

    private val _pets = MutableLiveData<List<Pet>>(emptyList())
    val pets: MutableLiveData<List<Pet>> = _pets

    private val _errorMessage = mutableStateOf("")
    val errorMessage: State<String> = _errorMessage

    init {
        checkUser()
    }

    // Checks if the user is logged in and fetches the pets if authenticated
    private fun checkUser() {
        val currentUserId = userRepository.getCurrentUserId()
        _isUserLoggedIn.value = currentUserId != null

        if (_isUserLoggedIn.value) {
            fetchPets()
        } else {
            _errorMessage.value = "User not logged in"
        }
    }

    // Fetches the pets for the logged-in user
    private fun fetchPets() {
        petRepository.getPetsForCurrentUser(
            onSuccess = { fetchedPets ->
                _pets.postValue(fetchedPets)
            },
            onFailure = { error ->
                _errorMessage.value = error
            }
        )
    }

    // Logs the user out and clears the pet list
    fun logout() {
        userRepository.logout()
        _isUserLoggedIn.value = false
        _pets.value = emptyList()
    }
}
