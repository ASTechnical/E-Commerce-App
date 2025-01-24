package com.ecommerceapp.presentation.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ecommerceapp.R
import com.ecommerceapp.databinding.FragmentChildrenBinding
import com.ecommerceapp.domain.interfaces.OnClick
import com.ecommerceapp.models.ItemModel
import com.ecommerceapp.presentation.adapters.ChildItemAdapter
import com.ecommerceapp.presentation.adapters.ImageAdapter
import com.ecommerceapp.presentation.adapters.SpecialOfferAdapter
import com.ecommerceapp.presentation.adapters.SpecialOfferAdapter2
import com.ecommerceapp.presentation.viewModel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChildrenFragment : Fragment(),OnClick {

    private var _binding: FragmentChildrenBinding? = null
    private val binding get() = _binding!!
    private val appViewModel: AppViewModel by viewModels()
    private lateinit var navController: NavController

    private lateinit var imageAdapter: ImageAdapter
    private lateinit var specialOfferAdapter: SpecialOfferAdapter
    private lateinit var specialOfferAdapter2: SpecialOfferAdapter2
    private lateinit var adapter: ChildItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildrenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpUi()
        imageAdapter = ImageAdapter(arrayListOf(), requireContext())
        binding.circleRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.circleRecyclerView.adapter = imageAdapter
        binding.circleRecyclerView.setHasFixedSize(true)

        adapter = ChildItemAdapter(arrayListOf(),this, requireContext())
        binding.mainRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.setHasFixedSize(true)
        //this line for 3rd adapter
        specialOfferAdapter = SpecialOfferAdapter(arrayListOf(), requireContext())
        binding.grideRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.grideRecyclerView.adapter = specialOfferAdapter
        binding.grideRecyclerView.setHasFixedSize(true)

        //this line for 4rth adapter
        specialOfferAdapter2 = SpecialOfferAdapter2(arrayListOf(), requireContext())
        binding.gride2RecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.gride2RecyclerView.adapter = specialOfferAdapter2
        binding.gride2RecyclerView.setHasFixedSize(true)






        appViewModel.products.observe(viewLifecycleOwner) { grideproduct2 ->
            specialOfferAdapter2.updateList(grideproduct2)

        }


        appViewModel.recommendedproduct.observe(viewLifecycleOwner) { images ->
            imageAdapter.updateList(images)

        }


        appViewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateList(products)

        }
        appViewModel.products.observe(viewLifecycleOwner) { grideproduct ->
            specialOfferAdapter.updateList(grideproduct)

        }


        appViewModel.errorMessage.observe(viewLifecycleOwner) {
            // Handle the error message, if needed
        }


        appViewModel.getProductData()
        appViewModel.getImageData()
        appViewModel.getGridProductData()
        appViewModel.getGridProductData2()
    }


    private fun setUpUi() {
        binding.all.setOnClickListener { navigateToFragment(R.id.homeFragment) }
        binding.trending.setOnClickListener { navigateToFragment(R.id.trendingFragment) }
        binding.childern.setOnClickListener { navigateToFragment(R.id.childrenFragment) }
        binding.recommended.setOnClickListener { navigateToFragment(R.id.recommendedFragment) }
    }

    private fun navigateToFragment(fragmentId: Int) {
        navController.navigate(fragmentId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun click(item: ItemModel) {
        TODO("Not yet implemented")
    }
}
