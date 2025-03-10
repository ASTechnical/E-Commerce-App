package com.ecommerceapp.presentation.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener {
    private val viewModel: AppViewModel by viewModels()
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isDarkMode: Boolean = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.fetchUserData()

        viewModel.userDataModel.observe(this) { userData ->
            if (userData != null) {
                binding!!.profileName.text = userData.name
                Log.d("MainActivity", "Profile Picture URL: $userData")
                //   Toast.makeText(this, "Profile Picture URL: $userData:", Toast.LENGTH_SHORT).show()
                userData.profileImageUrl?.let { url ->
                    Log.d("MainActivity", "Profile Picture URL: $url")
                    //  Toast.makeText(this, "Profile Picture URL: $url:", Toast.LENGTH_SHORT).show()
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error) // Add an error placeholder
                        .into(binding!!.profileImg)

                } ?: run {
                    binding!!.profileImg.setImageResource(R.drawable.profile)
                }
            } else {
                binding!!.profileName.text = "No User Data"
                binding!!.profileImg.setImageResource(R.drawable.ic_placeholder)
            }
        }

        // Observe errorMessage LiveData
        viewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
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

    private fun toastMessage(message: String) {
        // Use a Toast to show error message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    private fun initView() {

        binding!!.imageViewSettings.setOnClickListener {
            binding!!.mainDrawer.openDrawer(GravityCompat.START)
        }
        binding?.cart?.setOnClickListener {
            toastMessage("Coming Soon")

        }
        binding!!.navView.setNavigationItemSelectedListener(this)



        binding!!.home.setOnClickListener {
            navController.navigate(R.id.homeFragment)
            setBottomNavigationListener()
            binding?.relativeLayoutAbout?.visibility = View.VISIBLE
        }
        binding!!.category.setOnClickListener {
            navController.navigate(R.id.catogeryFragment)
            setBottomNavigationListener()
            binding?.relativeLayoutAbout?.visibility = View.GONE
        }
        binding!!.favtLayout.setOnClickListener {
            navController.navigate(R.id.favouriteFragment)
            setBottomNavigationListener()
            binding?.relativeLayoutAbout?.visibility = View.GONE
        }
        binding!!.account.setOnClickListener {
            navController.navigate(R.id.profileFragment)
            setBottomNavigationListener()
            binding?.relativeLayoutAbout?.visibility = View.GONE
        }
    }

    private fun setBottomNavigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigation(destination.id)
            updateBottomBar(destination.id)
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

        setIconAndTextColor(
            binding!!.homeIcon,
            binding!!.txtHome,
            destinationId == R.id.homeFragment
        )
        setIconAndTextColor(
            binding!!.categoryIcon,
            binding!!.categoryTxt,
            destinationId == R.id.catogeryFragment
        )
        setIconAndTextColor(
            binding!!.imgCart,
            binding!!.txtCart,
            destinationId == R.id.favouriteFragment
        )
        setIconAndTextColor(
            binding!!.accountIcon,
            binding!!.txtAccount,
            destinationId == R.id.profileFragment
        )

    }

    private fun updateBottomBar(destinationId: Int) {
        when (destinationId) {
            R.id.homeFragment,
            R.id.catogeryFragment,
            R.id.favouriteFragment,
            R.id.profileFragment -> {
                binding?.bottomNav?.visibility = View.VISIBLE
            }

        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // Navigate to Home
                navController.navigate(R.id.homeFragment)
                setBottomNavigationListener()
                closeDrawerWithDelay()
            }
            R.id.nav_category -> {
                // Navigate to Categories
                navController.navigate(R.id.catogeryFragment)
                setBottomNavigationListener()
                closeDrawerWithDelay()
            }
            R.id.nav_order -> {
                // Navigate to Orders
               // BnavController.navigate(R.id.homeFragment)
               // seottomNavigationListener()
                toastMessage("Order Clicked")
                closeDrawerWithDelay()
            }
            R.id.nav_favourite -> {
                navController.navigate(R.id.favouriteFragment)
                setBottomNavigationListener()
                closeDrawerWithDelay()
            }
            R.id.nav_cart -> {
                // Navigate to Cart
               toastMessage("Cart Clicked")
                closeDrawerWithDelay()
            }
            R.id.nav_accounts -> {
                // Navigate to Account
              navController.navigate(R.id.profileFragment)
                setBottomNavigationListener()
                closeDrawerWithDelay()
            }
            R.id.nav_darkmode -> {
                // Toggle Dark Mode
                toggleDarkMode()
                closeDrawerWithDelay()
            }
            R.id.nav_log -> {
                // Log out
                logout()
                closeDrawerWithDelay()
            }
            R.id.nav_shareApp -> {
                // Share the app
                shareApp()
                closeDrawerWithDelay()
            }

        }
        // Close the drawer after item selection
        binding!!.mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }



    // Method to toggle dark mode
    private fun toggleDarkMode() {
        isDarkMode = !isDarkMode
        val editor = sharedPreferences.edit()
        editor.putBoolean("isDarkMode", isDarkMode)
        editor.apply()

        // Apply dark mode or light mode based on the flag
        if (isDarkMode) {
            // setTheme(R.style.AppTheme_Dark)
        } else {
            // setTheme(R.style.AppTheme_Light)
        }

        // Restart activity to apply theme change
        recreate()
    }

    // Method to log out
    private fun logout() {
        // Clear user session or tokens
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Redirect to LoginActivity
        val intent = Intent(this, com.ecommerceapp.presentation.ui.activities.LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Method to share the app
    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this amazing app: https://play.google.com/store/apps/details?id=$packageName")
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    // Method to open app in Play Store
    private fun openAppInPlayStore(packageName: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
        }
    }


    private fun closeDrawerWithDelay() {
        binding!!.mainDrawer.postDelayed({ binding!!.mainDrawer.closeDrawer(GravityCompat.START) }, 300)
    }


}