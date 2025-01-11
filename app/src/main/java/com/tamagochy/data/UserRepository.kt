package com.tamagochy.data

import com.tamagochy.model.User

interface UserRepository {
    fun getCurrentUserId(): String?
    fun getUser(onSuccess: (User) -> Unit, onFailure: (String) -> Unit)
    fun createUserIfNotExists(onSuccess: () -> Unit, onFailure: (String) -> Unit)
    fun logout()
}

