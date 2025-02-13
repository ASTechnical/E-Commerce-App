package com.ecommerceapp.presentation.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.ecommerceapp.R
import com.ecommerceapp.databinding.FragmentProfileBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AppViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe the userData LiveData from the ViewModel
        viewModel.userDataModel.observe(viewLifecycleOwner, Observer { userData ->
            if (userData != null) {
                // Update profile name
                binding.profileName.text = userData.name

                // Log and show a toast with user profile data
                Log.d("ProfileFragment", "Profile Picture URL: ${userData.profileImageUrl}")
                // Toast.makeText(requireContext(), "Profile Picture URL: ${userData.profileImageUrl}", Toast.LENGTH_SHORT).show()

                // Load the profile image using Glide
                userData.profileImageUrl?.let { url ->
                    Log.d("ProfileFragment", "Loading profile picture from URL: $url")
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.ic_placeholder) // Placeholder while loading
                        .error(R.drawable.ic_error) // Error image if loading fails
                        .into(binding.profileImg) // Set the image to ImageView
                } ?: run {
                    // Set default profile image if URL is null
                    binding.profileImg.setImageResource(R.drawable.profile)
                }
            } else {
                // Handle the case where userData is null
                binding.profileName.text = "No User Data"
                binding.profileImg.setImageResource(R.drawable.ic_placeholder)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clean up binding reference to prevent memory leaks
    }
}
