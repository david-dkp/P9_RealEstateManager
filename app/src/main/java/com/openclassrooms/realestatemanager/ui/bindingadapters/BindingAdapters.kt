package com.openclassrooms.realestatemanager.ui.bindingadapters

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@BindingAdapter("app:imagePath", "app:circleCrop", "app:centerCrop", "app:cornerRadius", requireAll = false)
fun loadImageFromStoragePath(
    imageView: ImageView,
    imagePath: String?,
    circleCrop: Boolean?,
    centerCrop: Boolean?,
    cornerRadius: Float?
) {
    imagePath?.let {
        val requestBuilder = Glide.with(imageView)
            .load(Firebase.storage.getReference(imagePath))

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
}

@BindingAdapter("app:imageUri", "app:circleCrop", "app:centerCrop", "app:cornerRadius", requireAll = false)
fun loadImageFromUri(
    imageView: ImageView,
    uri: Uri?,
    circleCrop: Boolean?,
    centerCrop: Boolean?,
    cornerRadius: Float?
) {
    uri?.let {
        val requestBuilder = Glide.with(imageView)
            .load(uri)

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
}



