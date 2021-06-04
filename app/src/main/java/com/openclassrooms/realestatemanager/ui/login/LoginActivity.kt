package com.openclassrooms.realestatemanager.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityLoginBinding
import com.openclassrooms.realestatemanager.others.ErrorType
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
                binding.tvErrorLogin.visibility = View.VISIBLE

                val errorMessage = when ((it as Resource.Error).errorType) {
                    is ErrorType.WrongCredential -> getString(R.string.login_sign_in_error_text)
                    is ErrorType.Unknown -> (it.errorType as ErrorType.Unknown).message
                    else -> "Error"
                }

                binding.tvErrorLogin.text = errorMessage

            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            val emailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val passwordValid = password.length >= 6

            if (emailValid and passwordValid) {
                viewModel.login(email, password)
                return@setOnClickListener
            }

            if (!emailValid) {
                binding.inputEmail.error = getString(R.string.email_error)
            }

            if (!passwordValid) {
                binding.inputPassword.error =
                    getString(R.string.password_min_length_error, MINIMUM_PASSWORD_LENGTH)
            }
        }
    }

}