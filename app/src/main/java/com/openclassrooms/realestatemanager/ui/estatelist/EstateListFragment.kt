package com.openclassrooms.realestatemanager.ui.estatelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding

class EstateListFragment : Fragment() {

    private lateinit var binding: FragmentEstateListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateListBinding.inflate(layoutInflater)

        return binding.root
    }

}