package com.tamagochy.model

import android.content.Intent
import com.google.android.gms.tasks.Task
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

sealed class LoginUiState {
    object Empty : LoginUiState()
    object Loading : LoginUiState()
    data class LaunchGoogleSignIn(
        val signInIntent: Intent,
        val callback: (Task<GoogleSignInAccount>) -> Unit
    ) : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
    object NavigateToHome : LoginUiState()
}