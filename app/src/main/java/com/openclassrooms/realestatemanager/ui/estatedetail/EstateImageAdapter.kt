package com.openclassrooms.realestatemanager.ui.estatedetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.EstateImage
import com.openclassrooms.realestatemanager.databinding.ItemEstateImageBinding

class EstateImageAdapter(
    val listener: ((EstateImage) -> Unit)?
) : ListAdapter<EstateImage, EstateImageAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EstateImage>() {

            override fun areItemsTheSame(oldItem: EstateImage, newItem: EstateImage): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EstateImage, newItem: EstateImage): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_estate_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.estateImage = getItem(position)
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val binding = ItemEstateImageBinding.bind(rootView).also {
            it.root.setOnClickListener {
                listener?.invoke(getItem(adapterPosition))
            }
        }
    }
}