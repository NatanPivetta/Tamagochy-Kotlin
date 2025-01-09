package com.tamagochy.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.tamagochy.ui.theme.TamagochyKotlinTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.Navigation
import com.tamagochy.R
import com.tamagochy.auth.FirebaseAuthHelper
import com.tamagochy.model.Pet

class PetListFragment : Fragment() {

    private val pets = emptyList<Pet>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamagochyKotlinTheme {
                    PetListScreen(pets, )

                }
            }
        }
    }
}

@Composable
fun PetListScreen(pets: List<Pet>) {

    LazyColumn(modifier = Modifier.fillMaxSize()
        .statusBarsPadding()) {
        items(pets) { pet ->
            PetCard(
                petName = pet.name,
                petCode = pet.code,
                lastMeal = pet.lastMeal,
                onFeedClick = {},
                onEditClick = {},
                onDeleteClick = {}
            )
        }

    }


}

@Composable
fun PetCard(
    petName: String,
    petCode: String,
    lastMeal: String,
    onFeedClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF8E1) // Light cream color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Pet Image
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pet_placeholder), // Replace with your placeholder
                    contentDescription = "Pet Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )

                // Pet Info
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(text = petName, color = Color(0xFF2196F3))
                    Text(text = petCode)
                    Text(text = "Última refeição: $lastMeal", color = Color(0xFF64B5F6))
                }
            }

            // Action Buttons (placed below pet information)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onFeedClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.restaurant_24px),
                        contentDescription = "Feed Pet"
                    )
                }
                Button(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_square_24px),
                        contentDescription = "Edit Pet"
                    )
                }
                Button(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(Color.Red)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_24px),
                        contentDescription = "Delete Pet"
                    )
                }
            }
        }
    }
}




data class Pet(val name: String, val code: String, val lastMeal: String)