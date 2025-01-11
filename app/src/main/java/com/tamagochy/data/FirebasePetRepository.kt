package com.tamagochy.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.model.Pet

class FirebasePetRepository(private val userRepository: UserRepository): PetRepository {
    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
    private val petsRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("pets")

    override fun getPetsForCurrentUser(
        onSuccess: (List<Pet>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = userRepository.getCurrentUserId()
        if (userId == null) {
            onFailure("No user is logged in")
            return
        }

        // Fetch the user's pets from the users/{userId}/pets node
        usersRef.child(userId).child("pets").get()
            .addOnSuccessListener { userPetsSnapshot ->
                val petIds = userPetsSnapshot.children.mapNotNull { it.key }

                if (petIds.isNotEmpty()) {
                    fetchPetsFromIds(petIds, onSuccess, onFailure)
                } else {
                    onFailure("No pets found for the user")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.localizedMessage ?: "Failed to fetch user pets")
            }
    }

    private fun fetchPetsFromIds(
        petIds: List<String>,
        onSuccess: (List<Pet>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val petList = mutableListOf<Pet>()
        for (petId in petIds) {
            petsRef.child(petId).get()
                .addOnSuccessListener { snapshot ->
                    val pet = snapshot.getValue(Pet::class.java)
                    if (pet != null) {
                        petList.add(pet)
                    }
                    if (petList.size == petIds.size) {
                        onSuccess(petList)
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.localizedMessage ?: "Failed to fetch pet data")
                }
        }
    }

    override fun feedPet(pet: Pet, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        petsRef.child(pet.alphanumericCode).child("lastMeal").setValue(System.currentTimeMillis())
    }
}
