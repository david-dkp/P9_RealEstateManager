package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityEstateListBinding
import com.openclassrooms.realestatemanager.databinding.HeaderDrawerBinding
import com.openclassrooms.realestatemanager.ui.addestate.AddEstateActivity
import com.openclassrooms.realestatemanager.ui.estatedetail.EstateDetailViewModel
import com.openclassrooms.realestatemanager.ui.login.LoginActivity
import com.openclassrooms.realestatemanager.ui.map.MapActivity
import com.openclassrooms.realestatemanager.ui.settings.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstateListBinding
    private lateinit var headerBinding: HeaderDrawerBinding

    private val viewModel: EstateListViewModel by viewModel()

    private var detailViewModel: EstateDetailViewModel? = null

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_estate_list)

        if (binding.root.findViewById<View>(R.id.containerViewEstateDetail) != null) {
            detailViewModel = getViewModel()
        }

        headerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.header_drawer,
            binding.navView,
            false
        )

        binding.navView.addHeaderView(headerBinding.root)

        setSupportActionBar(binding.toolbar)

        setupDrawer()
        setupObservers()
    }


    fun setupDrawer() {
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer,
        )

        drawerToggle.drawerArrowDrawable.color = Color.WHITE

        binding.drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_map -> Intent(this, MapActivity::class.java).apply { startActivity(this) }
                R.id.item_logout -> viewModel.logout()
                R.id.item_settings -> Intent(
                    this,
                    SettingsActivity::class.java
                ).apply { startActivity(this) }
            }

            true
        }
    }

    @ExperimentalCoroutinesApi
    fun setupObservers() {
        lifecycleScope.launch {
            viewModel.isLoggedIn.collect {
                if (!it) {
                    withContext(Dispatchers.Main) {
                        Intent(
                            this@EstateListActivity,
                            LoginActivity::class.java
                        ).apply { startActivity(this) }
                        finish()
                    }
                }
            }
        }

        viewModel.user.observe(this) {
            headerBinding.user = it.data
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.estate_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.add_estate_item -> Intent(
                this,
                AddEstateActivity::class.java
            ).apply { startActivity(this) }
        }

        return true
    }

}