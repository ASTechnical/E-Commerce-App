package com.ecommerceapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ecommerceapp.databinding.ActivitySignInBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val viewModel: AppViewModel by viewModels()
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = auth.currentUser
        if (user != null && user.isEmailVerified) {
            // User is signed in and email is verified, navigate to HomeActivity
            navigateToHomeActivity()
        }

        binding.signIn.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Please enter a valid email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.goToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signInUser(email: String, password: String) {
        viewModel.signInWithEmailPassword(email, password) { userId ->
            if (userId != null) {
                Log.d("SignInActivity", "User signed in with ID: $userId")
                navigateToHomeActivity()
            } else {
                Toast.makeText(this, "Sign-In failed. Please check your email and password, or verify your email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
