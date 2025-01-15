package com.tamagochy.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tamagochy.model.Pet
import com.tamagochy.auth.FirebaseAuthHelper

object FirebaseDatabaseHelper {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.reference.child("users")
    private var petsRef: DatabaseReference = database.reference.child("pets")


    // Fetch pets for the current logged-in user
    fun getPetsForCurrentUser(onSuccess: (List<Pet>) -> Unit, onFailure: (String) -> Unit) {
        val currentUser = FirebaseAuthHelper.getCurrentUser()
        val userPetsRef = usersRef.child(currentUser?.uid ?: "").child("pets")
        if (currentUser != null) {
            // Step 1: Get the list of pet IDs from the user's pets field
            userPetsRef.get()
                .addOnSuccessListener { userSnapshot ->
                    // Extract keys (pet IDs) from the snapshot
                    val petIds = userSnapshot.children.mapNotNull { it.key }

                    if (petIds.isNotEmpty()) {
                        Log.d("FirebaseDatabaseHelper", "Pet IDs: $petIds")
                        // Step 2: Fetch the actual pet data from the pets collection
                        fetchPetsFromIds(petIds, onSuccess, onFailure)
                    } else {
                        onFailure("No pets found for the user")
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.localizedMessage ?: "Error retrieving user pets")
                }
        } else {
            onFailure("No user is logged in")
        }
    }

    // Check if user exists by email, if not create a new user
    fun checkAndCreateUser(
        onFailure: (String) -> Unit,
        onComplete: (Boolean) -> Unit
    ) {
        val currentUser =
            FirebaseAuthHelper.getCurrentUser() ?: return onFailure("User not logged in.")
        val userId = currentUser.uid

        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    // User already exists
                    onComplete(true)
                } else {
                    // Create user if not exists
                    val user = mapOf(
                        "email" to (currentUser.email ?: ""),
                        "username" to (currentUser.displayName ?: ""),
                        "useruid" to userId
                    )
                    userRef.setValue(user)
                        .addOnSuccessListener { onComplete(true) }
                        .addOnFailureListener { e -> onFailure("Error creating user: ${e.message}") }
                }
            }
            .addOnFailureListener { e -> onFailure("Error checking user existence: ${e.message}") }
    }


    private fun fetchPetsFromIds(
        petIds: List<String>,
        onSuccess: (List<Pet>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val petList = mutableListOf<Pet>()
        val petsRef = FirebaseDatabase.getInstance().reference.child("pets")
        // Fetch pet data for each pet ID
        for (petId in petIds) {
            Log.d("FirebaseDatabaseHelper", "Fetching pet data for ID: $petId")
            petsRef.child(petId).get()
                .addOnSuccessListener { snapshot ->
                    val pet = snapshot.getValue(Pet::class.java)
                    if (pet != null) {
                        pet.petUID = petId
                        petList.add(pet)
                    }
                    // If we have fetched all pets, call onSuccess
                    if (petList.size == petIds.size) {
                        onSuccess(petList)
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.localizedMessage ?: "Failed to fetch pet data")
                }
        }
    }

    fun feedPet(pet: Pet) {
        val petsRef = FirebaseDatabase.getInstance().reference.child("pets")
        petsRef.child(pet.petUID).child("lastMeal").setValue(System.currentTimeMillis())

    }
}

