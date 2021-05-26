package com.openclassrooms.realestatemanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityLoginBinding
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.ui.estate.estatelist.EstateListActivity
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {

    val viewModel by viewModels<LoginViewModel>()

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.viewmodel = viewModel
        setContentView(binding.root)

        setupGoogleSignIn()

        viewModel.loginState.observe(this) {
            if (it is Resource.Success) {
                Intent(this, EstateListActivity::class.java).apply { startActivity(this) }
                finish()
            }
        }
    }

    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        viewModel.onGoogleActivityResult(it)
    }

    private fun setupGoogleSignIn() {
        val googleOptions = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build();

        val googleClient = GoogleSignIn.getClient(this, googleOptions)

        binding.btnGoogleSignIn.setOnClickListener {
            googleSignInLauncher.launch(googleClient.signInIntent)
            viewModel.onStartGoogleIntent()
        }
    }

}