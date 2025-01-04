package com.tamagochy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tamagochy.ui.theme.TamagochyKotlinTheme
import com.tamagochy.model.Pet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamagochyKotlinTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Example list of pets
                    val pets = listOf(
                        Pet("Buddy", "#123456", "01/12/2024", "pet1.jpg"),
                        Pet("Charlie", "#654321", "01/12/2024", "pet2.jpg")
                    )
                    PetListScreen(pets)
                }
            }
        }
    }
}


@Composable
fun PetScreen(modifier: Modifier = Modifier) {
    // Apply the modifier to the outermost layout
    Column(modifier = modifier.fillMaxSize()) {
        // Your PetList or other composables
    }
}






@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TamagochyKotlinTheme {

    }
}