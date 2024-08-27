package com.ecommerceapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.ecommerceapp.databinding.ActivitySignUpBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: AppViewModel by viewModels()
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        binding.tvHaveAccount.setOnClickListener {
            navigateToSignInActivity()
        }

        binding.signUpButton.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val name = binding.userNameEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                signUpUser(email, password, name)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Please enter a valid email, password, and name", Toast.LENGTH_SHORT).show()
                Log.w("SignUpActivity", "Empty email, password, or name")
            }
        }
    }

    private fun signUpUser(email: String, password: String, name: String) {
        viewModel.signUpWithEmailPassword(email, password, name) { userId ->
            binding.progressBar.visibility = View.GONE
            if (userId != null) {
                Log.d("SignUpActivity", "User signed up with ID: $userId")
                Toast.makeText(this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show()

                // Sign out the user to prevent them from using the app without verification
                auth.signOut()

                // After sign-up, navigate to sign-in screen
                navigateToSignInActivity()
            }
        }
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
