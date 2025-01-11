package com.tamagochy.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.model.User

class FirebaseUserRepository : UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usersRef: DatabaseReference = database.reference.child("users")

    override fun getCurrentUserId(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    override fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val userId = getCurrentUserId() ?: return onFailure("No user logged in")
        usersRef.child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                if (user != null) onSuccess(user) else onFailure("User not found")
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: "Failed to fetch user") }
    }

    override fun createUserIfNotExists(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
            ?: return onFailure("No user logged in")

        val userRef = usersRef.child(currentUser.uid)
        userRef.get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    val user = User(
                        email = currentUser.email ?: "",
                        name = currentUser.displayName ?: "",
                        userUid = currentUser.uid
                    )
                    userRef.setValue(user)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { onFailure("Failed to create user") }
                } else {
                    onSuccess()
                }
            }
            .addOnFailureListener { onFailure(it.localizedMessage ?: "Failed to check user") }
    }

    override fun logout() {
        TODO("Not yet implemented")
    }
}
