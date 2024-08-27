package com.ecommerceapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ecommerceapp.R
import com.ecommerceapp.databinding.ActivityMainBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

//New Code After Update By Abu Saeed
@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: AppViewModel by viewModels()
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.fetchUserData()

        // Observe userData LiveData
        viewModel.userData.observe(this) { userData ->
            if (userData != null) {
                // Update UI with the fetched user data
                binding!!.profileName.text = userData.name

                // Load the profile picture if URL is provided
                if (userData.profilePictureUrl != null) {
                    Glide.with(this)
                        .load(userData.profilePictureUrl)
                        .placeholder(R.drawable.ic_placeholder)
                        .into(binding!!.profileImg)
                } else {
                    // Handle case where there is no profile picture
                    binding!!.profileImg.setImageResource(R.drawable.profile)
                }
            } else {
                // Handle case where userData is null
                binding!!.profileName.text = "No User Data"
                binding!!.profileImg.setImageResource(R.drawable.ic_placeholder)
            }
        }

        // Observe errorMessage LiveData
        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                // Show error message to user
                showError(errorMessage)
            }
        }

        // Observe isLoading LiveData
        viewModel.isLoading.observe(this) { isLoading ->
            // Show or hide the loading indicator
          //  binding.loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        navController = navHostFragment?.findNavController()
            ?: throw IllegalStateException("NavHostFragment not found")
        initView()
        setContentView(binding!!.root)
    }

    private fun showError(message: String) {
        // Use a Toast to show error message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

//


    private fun initView() {

        binding!!.imageViewSettings.setOnClickListener {
            binding!!.main.openDrawer(GravityCompat.START)
        }
        binding?.cart?.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        binding!!.navView.setNavigationItemSelectedListener(this)

        setBottomNavigationListener()
        binding!!.home.setOnClickListener { navController.navigate(R.id.homeFragment) }
        binding!!.category.setOnClickListener { navController.navigate(R.id.catogeryFragment) }
        binding!!.cart.setOnClickListener { navController.navigate(R.id.favouriteFragment) }
        binding!!.account.setOnClickListener { navController.navigate(R.id.profileFragment) }
    }

    private fun setBottomNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigation(destination.id)
            updateTopBar(destination.id)
        }
    }

    private fun updateBottomNavigation(destinationId: Int) {
        val selectedColor = ResourcesCompat.getColor(resources, R.color.colorselected, null)
        val defaultColor = ResourcesCompat.getColor(resources, R.color.white, null)

        fun setIconAndTextColor(icon: ImageView, text: TextView, isSelected: Boolean) {
            val color = if (isSelected) selectedColor else defaultColor
            icon.setColorFilter(color)
            text.setTextColor(color)
        }

        setIconAndTextColor(binding!!.homeIcon, binding!!.txtHome, destinationId == R.id.homeFragment)
        setIconAndTextColor(binding!!.categoryIcon, binding!!.categoryTxt, destinationId == R.id.catogeryFragment)
        setIconAndTextColor(binding!!.cart, binding!!.txtCart, destinationId == R.id.favouriteFragment)
        setIconAndTextColor(binding!!.accountIcon, binding!!.txtAccount, destinationId == R.id.profileFragment)

    }

    private fun updateTopBar(destinationId: Int) {
        when (destinationId) {
            R.id.homeFragment, R.id.catogeryFragment, R.id.favouriteFragment, R.id.profileFragment -> {
                binding?.bottomNav?.visibility = View.VISIBLE

            }

        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
/*
package com.ecommerceapp.ui.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ecommerceapp.R
import com.ecommerceapp.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        imageView = findViewById(R.id.profile_img)
        textView = findViewById(R.id.textView161)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        navController = navHostFragment?.findNavController()
            ?: throw IllegalStateException("NavHostFragment not found")

        initView()
        fetchData()
    }

    private fun initView() {
        binding!!.imageViewSettings.setOnClickListener {
            binding!!.main.openDrawer(GravityCompat.START)
        }
        binding?.cart?.setOnClickListener {
            Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
        }
        binding!!.navView.setNavigationItemSelectedListener(this)

        setBottomNavigationListener()
        binding!!.home.setOnClickListener { navController.navigate(R.id.homeFragment) }
        binding!!.category.setOnClickListener { navController.navigate(R.id.catogeryFragment) }
        binding!!.cart.setOnClickListener { navController.navigate(R.id.favouriteFragment) }
        binding!!.account.setOnClickListener { navController.navigate(R.id.profileFragment) }
    }



    private fun setBottomNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigation(destination.id)
            updateTopBar(destination.id)
        }
    }

    private fun updateBottomNavigation(destinationId: Int) {
        val selectedColor = ResourcesCompat.getColor(resources, R.color.colorselected, null)
        val defaultColor = ResourcesCompat.getColor(resources, R.color.white, null)

        fun setIconAndTextColor(icon: ImageView, text: TextView, isSelected: Boolean) {
            val color = if (isSelected) selectedColor else defaultColor
            icon.setColorFilter(color)
            text.setTextColor(color)
        }

        setIconAndTextColor(binding!!.homeIcon, binding!!.txtHome, destinationId == R.id.homeFragment)
        setIconAndTextColor(binding!!.categoryIcon, binding!!.categoryTxt, destinationId == R.id.catogeryFragment)
        setIconAndTextColor(binding!!.cart, binding!!.txtCart, destinationId == R.id.favouriteFragment)
        setIconAndTextColor(binding!!.accountIcon, binding!!.txtAccount, destinationId == R.id.profileFragment)
    }

    private fun updateTopBar(destinationId: Int) {
        when (destinationId) {
            R.id.homeFragment, R.id.catogeryFragment, R.id.favouriteFragment, R.id.profileFragment -> {
                binding?.bottomNav?.visibility = View.VISIBLE
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
*/
