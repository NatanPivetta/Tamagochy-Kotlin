package com.tamagochy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.ui.theme.TamagochyKotlinTheme
import com.tamagochy.model.Pet
import com.tamagochy.ui.PetListScreen
import com.tamagochy.navigation.Screen

class MainActivity : ComponentActivity() {



    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamagochyKotlinTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    var currentUser by remember { mutableStateOf(FirebaseAuthHelper.getCurrentUser()) }
                    // Example list of pets
                    val pets = listOf(
                        Pet("Buddy", "#123456", "01/12/2024", "pet1.jpg"),
                        Pet("Charlie", "#654321", "01/12/2024", "pet2.jpg")
                    )
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.Login.route) {
                        composable(Screen.Login.route) { LoginScreen(FirebaseAuthHelper.getGoogleSignInClient(context = LocalContext.current),
                            onLoginSuccess = { navController.navigate(Screen.Home.route) }) }
                        composable(Screen.Home.route) { PetScreen(pets, onLogout = { navController.navigate(Screen.Login.route) }) }
                    }

                    if(currentUser == null){
                        LoginScreen(googleSignInClient = FirebaseAuthHelper.getGoogleSignInClient(LocalContext.current),
                            onLoginSuccess = {
                                currentUser = FirebaseAuthHelper.getCurrentUser()
                            })
                    }else {
                        Log.d("MainActivity", "Current User: ${currentUser?.email}")
                        PetScreen(pets, onLogout = {})
                    }

                }
            }
        }
    }
}


@Composable
fun PetScreen(pets: List<Pet>, onLogout: () -> Unit) {
    val context = LocalContext.current
    // Apply the modifier to the outermost layout
    Column(modifier = Modifier.fillMaxSize()) {
        PetListScreen(pets)

    }
    Button(
        onClick = {
            // Perform sign-out logic
            FirebaseAuthHelper.signOut(context)
            // Show sign-out confirmation
            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show()
            // Navigate to the login screen or perform any other action
            onLogout()
            Log.d("MainActivity", "Signed out")


        },
        modifier = Modifier
            .height(16.dp)
            .width(32.dp)
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(text = "Log Out")
    }

}


@Composable
fun AppContent(){
    var currentUser by remember { mutableStateOf(FirebaseAuthHelper.getCurrentUser()) }

}





@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TamagochyKotlinTheme {
    }
}
