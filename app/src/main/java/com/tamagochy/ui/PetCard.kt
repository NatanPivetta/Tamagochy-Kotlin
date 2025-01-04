package com.tamagochy.ui



import androidx.compose.foundation.Image
import coil.compose.rememberImagePainter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.tamagochy.R
import com.tamagochy.model.Pet


@Composable
fun PetCard(pet: Pet) {
    val imageUrl = pet.imageUrl

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Load image from Firebase Storage using Glide
            if (imageUrl.isNotEmpty()) {
                val imageView = rememberAsyncImagePainter(imageUrl)
                Image(
                    painter = imageView,
                    contentDescription = "Pet Image",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder for missing image
                Image(
                    painter = painterResource(id = R.drawable.pet_placeholder),
                    contentDescription = "Placeholder",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = pet.name)
                Text(text = pet.code)
                Text(text = "Última refeição: ${pet.lastMeal}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetCardPreview() {
    PetCard(Pet("Buddy", "#123456", "01/12/2024", ""))
}


