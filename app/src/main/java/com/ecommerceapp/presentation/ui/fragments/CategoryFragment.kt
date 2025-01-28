package com.ecommerceapp.presentation.ui.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.ecommerceapp.R
import com.ecommerceapp.presentation.adapters.AccessoriesAdapter
import com.ecommerceapp.databinding.FragmentCatogeryBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var _binding: FragmentCatogeryBinding? = null
    private val binding get() = _binding!!
    private lateinit var specialOffersAdapter: AccessoriesAdapter

    private val appViewModel: AppViewModel by viewModels()
    private lateinit var context:Context
    private val args: CategoryFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatogeryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context= requireContext()
        setupRecyclerViews()
        setupCategoryClickListeners()
        val passedItem = args.itemModel
        Log.d("CategoryFragment", "Received item: $passedItem")

        appViewModel.specialOffers.observe(viewLifecycleOwner) { specialProducts ->
            Log.d("CategoryProducts", "Special Offers loaded: ${specialProducts.size}")
            specialOffersAdapter.updateList(specialProducts)
        }

        appViewModel.newProducts.observe(viewLifecycleOwner) { newProducts ->
            Log.d("CategoryProducts", "New Products loaded: ${newProducts.size}")
            specialOffersAdapter.updateList(newProducts)
        }

        appViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        specialOffersAdapter = AccessoriesAdapter(ArrayList(), context)


        // Use GridLayoutManager with spanCount of 4
        binding.specialOffersRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = specialOffersAdapter
        }

        binding.newProductsRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = specialOffersAdapter
        }
    }


   /* private fun setupCategoryClickListeners() {
        appViewModel.fetchCategoryProducts("T-Shirts")
        // Category click listeners to trigger fetching
        binding.TShirts.setOnClickListener {
            appViewModel.fetchCategoryProducts("T-Shirts")
        }

        binding.Accessories.setOnClickListener {
            appViewModel.fetchCategoryProducts("Accessories")
        }

        binding.Bakewear.setOnClickListener {
            appViewModel.fetchCategoryProducts("backware")
        }

        binding.HandBag.setOnClickListener {
            appViewModel.fetchCategoryProducts("bags")
        }

        binding.HomeKitchen.setOnClickListener {
            appViewModel.fetchCategoryProducts("home_kitchen")
        }

        binding.wintershoes.setOnClickListener {
            appViewModel.fetchCategoryProducts("winter_shoes")
        }

        binding.Women.setOnClickListener {
            appViewModel.fetchCategoryProducts("women")
        }

        binding.Men.setOnClickListener {
            appViewModel.fetchCategoryProducts("men")
        }
    }*/
   private fun setupCategoryClickListeners() {
       onCategorySelected(binding.Accessories, "Accessories")
       binding.TShirts.setOnClickListener {
           onCategorySelected(binding.TShirts, "T-Shirts")
       }

       binding.Accessories.setOnClickListener {
           onCategorySelected(binding.Accessories, "Accessories")
       }

       binding.Bakewear.setOnClickListener {
           onCategorySelected(binding.Bakewear, "backware")
       }

       binding.HandBag.setOnClickListener {
           onCategorySelected(binding.HandBag, "bags")
       }

       binding.HomeKitchen.setOnClickListener {
           onCategorySelected(binding.HomeKitchen, "home_kitchen")
       }

       binding.wintershoes.setOnClickListener {
           onCategorySelected(binding.wintershoes, "winter_shoes")
       }

       binding.Women.setOnClickListener {
           onCategorySelected(binding.Women, "women")
       }

       binding.Men.setOnClickListener {
           onCategorySelected(binding.Men, "men")
       }
   }

    private fun onCategorySelected(button: View, category: String) {
        // Reset the style for all buttons
        resetCategoryStyles()

        // Change the background color and make the text bold for the selected button
        button.setBackgroundColor(Color.parseColor("#121B22"))  // Change to your desired color
        if (button is TextView) {
            button.setTypeface(null, Typeface.BOLD)
        }

        // Fetch the products for the selected category
        appViewModel.fetchCategoryProducts(category)
    }

    private fun resetCategoryStyles() {
        // Reset background color and text style for all buttons
        val buttons = listOf(
            binding.TShirts, binding.Accessories, binding.Bakewear, binding.HandBag,
            binding.HomeKitchen, binding.wintershoes, binding.Women, binding.Men
        )

        for (button in buttons) {
            button.setBackgroundColor(resources.getColor(R.color.backgroungseletedcolor))  // Default background color
            button.setTypeface(null, Typeface.BOLD)
        }
    }

}