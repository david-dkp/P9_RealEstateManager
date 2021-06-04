package com.openclassrooms.realestatemanager.ui.estatelist

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.google.android.libraries.places.api.Places
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityEstateListBinding
import com.openclassrooms.realestatemanager.databinding.HeaderDrawerBinding
import com.openclassrooms.realestatemanager.others.EXTRA_ESTATE_ID
import com.openclassrooms.realestatemanager.others.FILTER_DIALOG_FRAGMENT_TAG
import com.openclassrooms.realestatemanager.others.KEY_FILTER_DATA
import com.openclassrooms.realestatemanager.others.SYNC_NOTIFICATION_CHANNEL_ID
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

    private var isMasterDetail = false

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createSyncNotificationChannel()

        Places.initialize(this, BuildConfig.MAPS_API_KEY)

        isMasterDetail = resources.getBoolean(R.bool.isMasterDetail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_estate_list)

        if (isMasterDetail) {
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

    private fun createSyncNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                SYNC_NOTIFICATION_CHANNEL_ID,
                getString(R.string.sync_notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = NotificationManagerCompat.from(this)
            notificationManager.createNotificationChannel(notificationChannel)
        }
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

        if (isMasterDetail) {
            viewModel.estates.observe(this) {
                it.firstOrNull()?.let { estate ->
                    detailViewModel?.setEstateId(estate.id)
                }
            }
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

            R.id.search_estate_item -> {
                val dialog =
                    (supportFragmentManager.findFragmentByTag(FILTER_DIALOG_FRAGMENT_TAG) as FilterDialogFragment?)
                        ?: FilterDialogFragment()

                if (dialog.isAdded || dialog.isVisible) return true

                dialog.arguments = bundleOf(
                    KEY_FILTER_DATA to viewModel.filterData.value
                )

                dialog.show(supportFragmentManager, FILTER_DIALOG_FRAGMENT_TAG)

            }

            R.id.edit_estate_item -> {
                Intent(this, AddEstateActivity::class.java).apply {
                    putExtra(EXTRA_ESTATE_ID, detailViewModel?.estateId?.value)
                    startActivity(this)
                }
                return true
            }
        }

        return true
    }

}