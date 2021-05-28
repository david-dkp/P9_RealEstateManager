package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListFragment : Fragment() {

    private val viewModel: EstateListViewModel by viewModel()

    private lateinit var binding: FragmentEstateListBinding

    private var isPaneMode = false

    private val adapter = EstateListAdapter {
        Intent(context, EstateDetailActivity::class.java).apply {
            putExtra(EXTRA_ESTATE_ID, it.id)
            startActivity(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateListBinding.inflate(layoutInflater)

        binding.containerView?.let { isPaneMode = true }

        //Setup list
        binding.rvEstates.adapter = adapter
        binding.rvEstates.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel.estates.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

}