package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.models.User
import com.openclassrooms.realestatemanager.databinding.ActivityEstateListBinding
import com.openclassrooms.realestatemanager.databinding.HeaderDrawerBinding
import com.openclassrooms.realestatemanager.ui.login.LoginActivity
import com.openclassrooms.realestatemanager.ui.settings.SettingsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEstateListBinding

    private val viewModel: EstateListViewModel by viewModel()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_estate_list)

        val headerBinding: HeaderDrawerBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.header_drawer,
            binding.navView,
            false
        )
        
        binding.navView.addHeaderView(headerBinding.root)

        headerBinding.user = User(
            "no",
            "david.dekeuwer@gmail.com",
            "David",
            "Dekeuwer",
            "0781923016",
            "profile_images/CWMVuHGwipH0EOut7xaS.jpg",
            false
        )

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

        drawerToggle.drawerArrowDrawable.color = Color.WHITE;

        binding.drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_logout -> viewModel.logout()
                R.id.item_settings -> Intent(this, SettingsActivity::class.java).apply { startActivity(this) }
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
                        Intent(this@EstateListActivity, LoginActivity::class.java).apply { startActivity(this) }
                        finish()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.estate_list_menu, menu)
        return true
    }

}