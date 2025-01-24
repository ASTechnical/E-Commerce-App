package com.ecommerceapp.ui.activities

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: AppViewModel by viewModels()
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

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
        binding.view.setOnClickListener {
            openImagePicker()
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
                if (imageUri != null) {
                    uploadProfilePicture(userId, imageUri!!, { downloadUrl ->
                        // Save the download URL along with user data in Firestore using ViewModel
                        viewModel.addUserToFirestore(userId, name, downloadUrl) { success ->
                            if (success) {
                                Toast.makeText(this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                navigateToSignInActivity()
                            } else {
                                Toast.makeText(this, "Error saving user data", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }, { e ->
                        Log.e("SignUpActivity", "Failed to upload profile picture", e)
                    })
                } else {
                    // If no image is selected, proceed with sign-up
                    viewModel.addUserToFirestore(userId, name, null) { success ->
                        if (success) {
                            Toast.makeText(this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show()
                            auth.signOut()
                            navigateToSignInActivity()
                        } else {
                            Toast.makeText(this, "Error saving user data", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }


    private fun saveUserData(userId: String, name: String, profileImageUrl: String?) {
        viewModel.addUserToFirestore(userId, name, profileImageUrl) { success ->
            if (success) {
                Toast.makeText(this, "Verification email sent. Please verify your email.", Toast.LENGTH_SHORT).show()
                auth.signOut()
                navigateToSignInActivity()
            } else {
                Toast.makeText(this, "Error saving user data", Toast.LENGTH_SHORT).show()
            }
        }
    }

   /* private fun signUpUser(email: String, password: String, name: String) {
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
    }*/
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.view.setImageURI(imageUri)
        }
    }
    private fun uploadProfilePicture(userId: String, uri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference.child("profile_pictures").child("$userId.jpg")
        storageReference.putFile(uri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener { downloadUrl ->
                    onSuccess(downloadUrl.toString())
                }.addOnFailureListener { e ->
                    onFailure(e)
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}
