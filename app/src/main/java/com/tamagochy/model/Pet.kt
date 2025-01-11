package com.tamagochy.model

data class Pet(
    val name: String = "",
    val alphanumericCode: String = "",
    val lastMeal: Long = 0,
    val imageUrl: String = "", // Path to image in Firebase Storage
    val petUid: String = "",
    val tutorID: List<String> = emptyList(),
)

