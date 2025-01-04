package com.tamagochy.utils



import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// Function to load image from Firebase Storage
fun loadImageFromFirebase(url: String, imageView: ImageView) {
    Glide.with(imageView.context)
        .load(url)
        .apply(RequestOptions().centerCrop()) // Optional
        .into(imageView)
}

// Function to get image download URL
fun getImageDownloadUrl(imagePath: String, callback: (String) -> Unit) {
    val storageReference: StorageReference = FirebaseStorage.getInstance().reference.child(imagePath)

    storageReference.downloadUrl.addOnSuccessListener { uri ->
        callback(uri.toString())
    }.addOnFailureListener {
        callback("")
    }
}
