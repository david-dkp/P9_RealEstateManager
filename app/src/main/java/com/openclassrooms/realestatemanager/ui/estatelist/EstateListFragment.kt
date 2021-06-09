package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class EstateListFragment : Fragment() {

    private val viewModel: EstateListViewModel by sharedViewModel()

    private lateinit var binding: FragmentEstateListBinding

    private var isMasterDetail = false

    private lateinit var adapter: EstateListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        isMasterDetail = resources.getBoolean(R.bool.master_detail)

        binding = FragmentEstateListBinding.inflate(layoutInflater)

        setupList()
        setupObservers()

        return binding.root
    }

    private fun setupList() {
        adapter = EstateListAdapter { estate ->
            viewModel.selectEstate(estate.id)
            if (!isMasterDetail) {
                Intent(context, EstateDetailActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }

        binding.rvEstates.adapter = adapter
        binding.rvEstates.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refreshEstates()
        }
    }

    private fun setupObservers() {

        viewModel.estates.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        if (isMasterDetail) {
            viewModel.selectedEstateId.observe(viewLifecycleOwner) {
                adapter.selectItem(it)
            }
        }

        viewModel.refreshState.observe(viewLifecycleOwner) {
            if (it !is Resource.Loading) {
                binding.swipeToRefresh.isRefreshing = false
            }
        }
    }

}