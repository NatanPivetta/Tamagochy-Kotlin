package com.tamagochy.model

data class Pet(
    val name: String,
    val code: String,
    val lastMeal: String,
    val imageUrl: String // Path to image in Firebase Storage
)
