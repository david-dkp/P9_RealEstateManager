package com.openclassrooms.realestatemanager.ui.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop

object BindingAdapters {

    @BindingAdapter("app:imagePath", "app:circleCrop", "app:centerCrop", requireAll = false)
    fun loadImageFromStoragePath(imageView: ImageView, imagePath: String?, circleCrop: Boolean? = false, centerCrop: Boolean? = true) {
        imagePath?.let {
            val requestBuilder = Glide.with(imageView)
                .load(it)

            if (centerCrop == true) {
                requestBuilder.centerCrop()
            }

            if (circleCrop == true) {
                requestBuilder.transform(CircleCrop())
            }

            requestBuilder.into(imageView)
        }
    }

}