package com.tamagochy.data

import com.tamagochy.model.Pet

class PetRepository constructor(private val FirebaseDatabaseHelper: FirebaseDatabaseHelper){
    fun getPetsForCurrentUser(onSuccess: (List<Pet>) -> Unit, onFailure: (String) -> Unit) {
        FirebaseDatabaseHelper.getPetsForCurrentUser(onSuccess, onFailure)
    }
    fun feedPet(pet: Pet, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        FirebaseDatabaseHelper.feedPet(pet)
    }

}

