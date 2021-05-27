package com.openclassrooms.realestatemanager.ui.estatelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityEstateListBinding
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
        binding = ActivityEstateListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupDrawer()
        setupObservers()
    }

    lateinit var drawerToggle: ActionBarDrawerToggle

    fun setupDrawer() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            R.string.open_drawer,
            R.string.close_drawer,
        )

        binding.drawer.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}