package com.tamagochy.data

import com.tamagochy.model.Pet

interface PetRepository {
    fun getPetsForCurrentUser(onSuccess: (List<Pet>) -> Unit, onFailure: (String) -> Unit)
    fun feedPet(pet: Pet, onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun fetchPetsForCurrentUser(onSuccess: (List<Pet>) -> Unit, onFailure: (String) -> Unit)

}

