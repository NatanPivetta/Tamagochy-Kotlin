package com.tamagochy.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.data.FirebaseDatabaseHelper
import com.tamagochy.model.Pet
import com.tamagochy.ui.components.PetCard

@Composable
fun PetListScreen(
    pets: List<Pet>,
    onLogout: () -> Unit,
    onFeedClick: (Pet) -> Unit,
    onEditClick: (Pet) -> Unit,
    onDeleteClick: (Pet) -> Unit
) {
    val pets = remember { mutableStateOf<List<Pet>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        isLoading.value = false
    }

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
        if (isLoading.value) {
            Text("Loading...")
        } else if (errorMessage.value != null) {
            Text("Error: ${errorMessage.value}")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(pets.value) { pet ->
                    PetCard(
                        pet = pet,
                        onFeedClick = { onFeedClick(pet) },
                        onEditClick = { onEditClick(pet) },
                        onDeleteClick = { onDeleteClick(pet) }
                    )
                }
            }

            Button(
                onClick = { onLogout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Logout")
            }
        }
    }
}