package com.openclassrooms.realestatemanager.ui.estatedetail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityEstateDetailBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstateDetailBinding

    private val viewModel: EstateDetailViewModel by viewModel()

    private val estateId by lazy {
        intent.getStringExtra(EXTRA_ESTATE_ID)!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_estate_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        viewModel.setEstateId(estateId)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.estate_detail_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.edit_estate_item -> {
                Intent(this, AddEstateActivity::class.java).apply {
                    putExtra(EXTRA_ESTATE_ID, viewModel.estateId.value)
                    startActivity(this)
                }
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}