package com.tamagochy

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.ui.theme.TamagochyKotlinTheme

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamagochyKotlinTheme {
                    LoginScreen(
                        googleSignInClient = FirebaseAuthHelper.getGoogleSignInClient(requireContext()),
                        onLoginSuccess = { requireActivity().recreate() }
                    )
                }
            }
        }
    }
}

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
