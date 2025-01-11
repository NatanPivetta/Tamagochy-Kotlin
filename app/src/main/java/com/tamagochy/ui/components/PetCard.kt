package com.tamagochy.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.tamagochy.R
import com.tamagochy.model.Pet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun PetCard(pet: Pet,
            onFeedClick: () -> Unit,
            onEditClick: () -> Unit,
            onDeleteClick: () -> Unit) {

    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val date = Date(pet.lastMeal)
    val petLastMealFormatted =    dateFormatter.format(date)


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
                    Text(text = pet.name, color = Color(0xFF2196F3))
                    Text(text = pet.alphanumericCode)
                    Text(text = "Última refeição: $petLastMealFormatted", color = Color(0xFF64B5F6))
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



