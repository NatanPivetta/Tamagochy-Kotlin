package com.tamagochy.model

data class User(
    val email: String = "",
    val name: String = "",
    val pets: List<String> = emptyList() // List of pet IDs
)
