package com.openclassrooms.realestatemanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityLoginBinding
import com.openclassrooms.realestatemanager.others.MINIMUM_PASSWORD_LENGTH
import com.openclassrooms.realestatemanager.others.Resource
import com.openclassrooms.realestatemanager.ui.estatelist.EstateListActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    val viewModel: LoginViewModel by viewModel()

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupGoogleSignIn()

        viewModel.loginState.observe(this) {
            if (it is Resource.Success) {
                Intent(this, EstateListActivity::class.java).apply { startActivity(this) }
                finish()
            } else if (it is Resource.Loading) {
                binding.scrim.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.scrim.visibility = View.INVISIBLE
                binding.progressBar.visibility = View.INVISIBLE
                binding.tvErrorLogin.setText(R.string.login_sign_in_error_text)
                binding.tvErrorLogin.visibility = View.VISIBLE
            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val emailValid =  Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val passwordValid = password.length >= 6

            if (emailValid and passwordValid) {
                viewModel.login(email, password)
                return@setOnClickListener
            }

            if (!emailValid) {
                binding.inputEmail.error = getString(R.string.email_error)
            }

            if (!passwordValid) {
                binding.inputPassword.error = getString(R.string.password_min_length_error, MINIMUM_PASSWORD_LENGTH)
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