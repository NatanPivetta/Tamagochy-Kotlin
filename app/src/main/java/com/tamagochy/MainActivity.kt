package com.tamagochy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.data.FirebaseDatabaseHelper
import com.tamagochy.model.Pet
import com.tamagochy.navigation.Screen
import com.tamagochy.ui.screens.LoginScreen
import com.tamagochy.ui.screens.PetListScreen
import com.tamagochy.ui.screens.onFeedClick
import com.tamagochy.ui.theme.TamagochyKotlinTheme



class MainActivity : ComponentActivity() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val pets = mutableListOf<Pet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TamagochyKotlinTheme {
                val navController = rememberNavController()

                // Check user authentication
                val isUserLoggedIn = remember {
                    mutableStateOf(currentUser != null)
                }

                NavHost(
                    navController = navController,
                    startDestination = if (isUserLoggedIn.value) Screen.Home.route else Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            googleSignInClient = FirebaseAuthHelper.getGoogleSignInClient(this@MainActivity),
                            onLoginSuccess = {
                                isUserLoggedIn.value = true
                                currentUser
                                    ?.let { it1 -> Log.d("MainActivity", it1.uid) }
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Home.route) {
                        val pets  = remember { mutableStateOf<List<Pet>>(emptyList()) }
                        FirebaseDatabaseHelper.checkAndCreateUser(onFailure = {errorMessage ->
                            run {
                                if (currentUser != null) {
                                    Log.d("MainActivity", "Error checking user existence: ${currentUser.uid}")
                                }
                                Log.d("MainActivity", "Error creating user: $errorMessage")
                            }
                        },
                            onComplete = {userExists ->
                                run {
                                    if (userExists) {
                                        FirebaseDatabaseHelper.getPetsForCurrentUser(onFailure = { errorMessage ->
                                            Log.d(
                                                "MainActivity",
                                                "Error getting pets: $errorMessage"
                                            )
                                        }, onSuccess = { fetchedPets ->
                                            pets.value = fetchedPets
                                            Log.d("MainActivity", "Fetched pets: $fetchedPets")
                                        })
                                    }
                                }
                            })
                        PetListScreen(
                            pets.value, // Replace with actual pets data
                            onLogout = {
                                FirebaseAuthHelper.signOut(this@MainActivity)
                                isUserLoggedIn.value = false
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            onDeleteClick = {pet ->
                                Log.d("MainActivity", "Deleting pet: ${pet.name}")
                            },
                            onEditClick = {pet ->
                                Log.d("MainActivity", "Editing pet: ${pet.name}")
                            },
                            onFeedClick = {pet ->
                               onFeedClick(pet)
                            }
                        )
                    }
                }
            }
        }
    }
}







@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TamagochyKotlinTheme {
    }
}
