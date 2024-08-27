package com.ecommerceapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ecommerceapp.databinding.ActivityCodeVerificationBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCodeVerificationBinding
    private val viewModel: AppViewModel by viewModels()

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
binding.verifyButton.setOnClickListener{
    checkEmailVerification()
}

    }

    private fun checkEmailVerification() {
        val user = auth.currentUser

        if (user != null) {
            // Reload user to get the updated status
            user.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Check if the email is verified
                    if (user.isEmailVerified) {
                        // Navigate to the home screen if email is verified
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        // Show a message if the email is not verified
                        Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle reload failure
                    Toast.makeText(this, "Failed to reload user information", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Handle case where user is not signed in
            Toast.makeText(this, "No user is signed in", Toast.LENGTH_SHORT).show()
            // Optionally navigate to login screen or other appropriate action
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
        // In VerificationActivity
        private fun getUserDetailsFromIntent(): Triple<String, String, String>? {
            val email = intent.getStringExtra("EMAIL")
            val password = intent.getStringExtra("PASSWORD")
            val name = intent.getStringExtra("NAME")

            return if (email != null && password != null && name != null) {
                Triple(email, password, name)
            } else {
                null
            }
        }
    }
    /*private lateinit var binding: ActivityCodeVerificationBinding
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCodeVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userName = intent.getStringExtra("userName")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val verificationId = intent.getStringExtra("verificationId")

        // Ensure that none of these values are null
        if (userName != null && phoneNumber != null && verificationId != null) {
            binding.verifyCodeButton.setOnClickListener {
                val smsCode = binding.verificationCodeEditText.text.toString().trim()

                if (smsCode.isNotEmpty()) {
                    // Create PhoneAuthCredential
                    val credential = PhoneAuthProvider.getCredential(verificationId, smsCode)

                    appViewModel.verifyCode(
                        credential = credential,
                        userName = userName,
                        phoneNumber = phoneNumber,
                        onSuccess = {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Verification Successful", Toast.LENGTH_SHORT).show()
                            // Navigate to another activity if needed
                        },
                        onFailure = { exception ->
                            binding.progressBar.visibility = View.GONE
                            binding.errorTextView.visibility = View.VISIBLE
                            binding.errorTextView.text = exception.localizedMessage
                            Log.e("CodeVerificationActivity", "Error verifying code", exception)
                        }
                    )
                } else {
                    Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Missing required data", Toast.LENGTH_SHORT).show()
            finish() // Optionally close the activity if data is missing
        }
    }*/

