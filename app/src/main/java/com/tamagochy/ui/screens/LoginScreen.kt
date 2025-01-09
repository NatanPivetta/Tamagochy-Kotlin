package com.tamagochy.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.tamagochy.auth.FirebaseAuthHelper

@Composable
fun LoginScreen(
    googleSignInClient: GoogleSignInClient,
    onLoginSuccess: () -> Unit
)
{


    val launcher = rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                FirebaseAuthHelper.signInWithGoogle(account) { success, error ->
                    if (success) onLoginSuccess()
                    else {
                        // Handle error
                    }
                }
            } catch (e: ApiException) {
                // Handle Google sign-in failure
            }
        }
    }

    Column(modifier = Modifier.statusBarsPadding()) {
        // UI code for email/password login and Google login
        Button(onClick = {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }) {
            Text("Sign in with Google")
        }
    }
}