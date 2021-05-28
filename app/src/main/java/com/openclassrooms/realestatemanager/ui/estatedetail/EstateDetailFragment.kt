package com.openclassrooms.realestatemanager.ui.estatedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.databinding.FragmentEstateDetailBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailFragment : Fragment() {

    private lateinit var binding: FragmentEstateDetailBinding

    private val viewModel: EstateDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEstateDetailBinding.inflate(layoutInflater)

        lifecycleScope.launchWhenStarted {
            viewModel.estate.collect {
                binding.estate = it
            }
        }

        return binding.root
    }

}