package com.ecommerceapp.presentation.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.ecommerceapp.presentation.adapters.ChildItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import com.ecommerceapp.databinding.FragmentFavouriteBinding
import com.ecommerceapp.domain.viewmodel.AppViewModel
import androidx.fragment.app.viewModels
import com.ecommerceapp.domain.interfaces.OnClick
import com.ecommerceapp.models.ItemModel

@AndroidEntryPoint
class FavouriteFragment : Fragment(),OnClick {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!


    private val appViewModel: AppViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: ChildItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)


        adapter = ChildItemAdapter(arrayListOf(), this,requireContext())
        binding.mainRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.setHasFixedSize(true)


        appViewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateList(products)

        }

        appViewModel.errorMessage.observe(viewLifecycleOwner) {
            // Handle the error message, if needed
        }


        appViewModel.getProductData()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun click(item: ItemModel) {
        val action = FavouriteFragmentDirections.actionFavouriteFragmentToCatogeryFragment(item)
        navController.navigate(action)
    }
}