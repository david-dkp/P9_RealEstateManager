package com.openclassrooms.realestatemanager.ui.bindingadapters

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@BindingAdapter(
    "app:imagePath",
    "app:imageUri",
    "app:circleCrop",
    "app:centerCrop",
    "app:cornerRadius",
    requireAll = false
)
fun loadImageFromUriOrPath(
    imageView: ImageView,
    imagePath: String?,
    imageUri: String?,
    circleCrop: Boolean?,
    centerCrop: Boolean?,
    cornerRadius: Float?
) {
    val requestBuilder: RequestBuilder<Drawable>

    if (imagePath != null && imageUri != null) {
        requestBuilder = Glide.with(imageView).load(Firebase.storage.getReference(imagePath))
    } else if (imagePath != null) {

        if (imagePath.isEmpty()) return

        requestBuilder = Glide.with(imageView).load(Firebase.storage.getReference(imagePath))
    } else if (imageUri != null) {
        requestBuilder = Glide.with(imageView).load(Uri.parse(imageUri))
    } else {
        return
    }

    val transformations = arrayListOf<BitmapTransformation>()

    if (centerCrop == true) {
        transformations.add(CenterCrop())
    }

    if (circleCrop == true) {
        transformations.add(CircleCrop())
    }

    cornerRadius?.let {
        transformations.add(RoundedCorners(cornerRadius.toInt()))
    }

    if (transformations.isNotEmpty()) requestBuilder.transform(MultiTransformation(transformations))
    requestBuilder.into(imageView)
}




