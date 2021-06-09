package com.openclassrooms.realestatemanager.ui.estatelist

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.domain.Estate
import com.openclassrooms.realestatemanager.databinding.ItemEstateBinding


class EstateListAdapter(
    val listener: ((Estate) -> Unit)
) : ListAdapter<Estate, EstateListAdapter.ViewHolder>(DIFF_CALLBACK) {

    var selectedItemPosition: Int = -1

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Estate>() {

            override fun areItemsTheSame(oldItem: Estate, newItem: Estate): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Estate, newItem: Estate): Boolean {
                return oldItem == newItem
            }

        }
    }

    fun selectItem(id: String) {
        notifyItemChanged(selectedItemPosition)
        selectedItemPosition = findItemPosById(id)
        notifyItemChanged(selectedItemPosition)
    }

    private fun findItemPosById(id: String): Int {
        return currentList.indexOfFirst { it.id == id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_estate, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estate = getItem(position)
        holder.binding.estate = estate

        holder.binding.cardEstate.isSelected = position == selectedItemPosition
    }

    inner class ViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        val binding = ItemEstateBinding.bind(rootView)

        init {
            binding.root.setOnClickListener {
                listener(getItem(adapterPosition))
            }
        }
    }
}