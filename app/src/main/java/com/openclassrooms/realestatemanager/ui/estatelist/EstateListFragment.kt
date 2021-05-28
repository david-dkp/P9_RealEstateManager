package com.openclassrooms.realestatemanager.ui.estatelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListFragment : Fragment() {

    private val viewModel: EstateListViewModel by viewModel()

    private lateinit var binding: FragmentEstateListBinding

    private val adapter = EstateListAdapter {
        Log.d("debug", "Clicked: $it")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateListBinding.inflate(layoutInflater)
        binding.rvEstates.adapter = adapter

        binding.rvEstates.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.estates.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

}