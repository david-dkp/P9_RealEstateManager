package com.openclassrooms.realestatemanager.ui.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

@BindingAdapter("app:imagePath", "app:circleCrop", "app:centerCrop", requireAll = false)
fun loadImageFromStoragePath(
    imageView: ImageView,
    imagePath: String?,
    circleCrop: Boolean? = false,
    centerCrop: Boolean? = true
) {
    imagePath?.let {
        val requestBuilder = Glide.with(imageView)
            .load(Firebase.storage.getReference(imagePath))

        if (centerCrop == true) {
            requestBuilder.centerCrop()
        }

        if (circleCrop == true) {
            requestBuilder.transform(CircleCrop())
        }

        requestBuilder.into(imageView)
    }
}

