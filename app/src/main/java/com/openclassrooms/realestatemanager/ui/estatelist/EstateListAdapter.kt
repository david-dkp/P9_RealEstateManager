package com.openclassrooms.realestatemanager.ui.estatelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.Estate
import com.openclassrooms.realestatemanager.databinding.ItemEstateBinding


class EstateListAdapter(
    val listener: ((Estate) -> Unit)
) : ListAdapter<Estate, EstateListAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Estate>() {

            override fun areItemsTheSame(oldItem: Estate, newItem: Estate): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Estate, newItem: Estate): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_estate, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.estate = getItem(position)
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