package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import com.openclassrooms.realestatemanager.databinding.FragmentEstateListBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailActivity
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListFragment : Fragment() {

    private val viewModel: EstateListViewModel by viewModel()

    private lateinit var binding: FragmentEstateListBinding

    private var estateDetailViewModel: EstateDetailViewModel? = null

    private val adapter = EstateListAdapter { estate ->
        estateDetailViewModel?.let {
            estateDetailViewModel!!.setEstateId(estate.id)
        } ?: Intent(context, EstateDetailActivity::class.java).apply {
            putExtra(EXTRA_ESTATE_ID, estate.id)
            startActivity(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEstateListBinding.inflate(layoutInflater)

        binding.containerViewEstateDetail?.let {
            estateDetailViewModel = getSharedViewModel()
        }

        //Setup list
        binding.rvEstates.adapter = adapter
        binding.rvEstates.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        lifecycleScope.launchWhenStarted {
            viewModel.estates.collect {
                withContext(Dispatchers.Main) {
                    adapter.submitList(it)
                }
            }
        }

        return binding.root
    }

}