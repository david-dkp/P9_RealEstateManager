package com.openclassrooms.realestatemanager.ui.estatedetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityEstateDetailBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstateDetailBinding

    private val viewModel: EstateDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val estateId = intent.getStringExtra(EXTRA_ESTATE_ID)!!

        viewModel.setEstateId(estateId)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_estate_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}