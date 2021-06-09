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
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel.loginState.observe(this) {
            when (it) {
                is Resource.Success -> {
                    Intent(this, EstateListActivity::class.java).apply { startActivity(this) }
                    finish()
                }
                is Resource.Loading -> {
                    binding.loadingOverlay.visibility = View.VISIBLE
                }
                else -> {
                    binding.loadingOverlay.visibility = View.INVISIBLE
                    binding.tvErrorLogin.visibility = View.VISIBLE

                    val errorMessage = when ((it as Resource.Error).errorType) {
                        is ErrorType.WrongCredential -> getString(R.string.login_sign_in_error_text)
                        is ErrorType.Unknown -> (it.errorType as ErrorType.Unknown).message
                        else -> null
                    }

                    errorMessage?.let { binding.tvErrorLogin.text = it }

                }
            }
        }

    }

}