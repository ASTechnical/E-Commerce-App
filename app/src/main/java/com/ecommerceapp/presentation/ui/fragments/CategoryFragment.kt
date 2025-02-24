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
        Log.d("CatogeryFragment", "Received item: $passedItem")

        appViewModel.specialOffers.observe(viewLifecycleOwner) { specialProducts ->
            Log.d("CategoryProducts", "Special Offers loaded: ${specialProducts.size}")
            specialOffersAdapter.updateList(specialProducts)
          //  binding.specialOffersRecyclerView1.visibility = View.GONE

        }

        appViewModel.newProducts.observe(viewLifecycleOwner) { newProducts ->
            Log.d("CategoryProducts", "New Products loaded: ${newProducts.size}")
            specialOffersAdapter.updateList(newProducts)
           // binding.newProductsRecyclerView.visibility = View.GONE
        }

        appViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        specialOffersAdapter = AccessoriesAdapter(ArrayList(), context)

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
        val categoryPairs = listOf(
            Pair(binding.TShirts, "T-Shirts"),
            Pair(binding.Accessories, "Accessories"),
            Pair(binding.Bakewear, "backware"),
            Pair(binding.HandBag, "bags"),
            Pair(binding.HomeKitchen, "home_kitchen"),
            Pair(binding.wintershoes, "winter_shoes"),
            Pair(binding.Women, "women"),
            Pair(binding.Men, "men")
        )
        categoryPairs.forEach { (button, category) ->
            button.setOnClickListener {
                onCategorySelected(button, category)
                appViewModel.fetchCategoryProducts(category)
            }
        }
    }


    private fun onCategorySelected(button: View, category: String) {
        resetCategoryStyles()
        button.setBackgroundColor(Color.parseColor("#121B22"))
        if (button is TextView) {
            button.setTypeface(null, Typeface.BOLD)
        }

        appViewModel.fetchCategoryProducts(category)
    }

    private fun resetCategoryStyles() {
        val buttons = listOf(
            binding.TShirts, binding.Accessories, binding.Bakewear, binding.HandBag,
            binding.HomeKitchen, binding.wintershoes, binding.Women, binding.Men
        )

        for (button in buttons) {
            button.setBackgroundColor(resources.getColor(R.color.backgroungseletedcolor))
            button.setTypeface(null, Typeface.BOLD)
        }
    }

}