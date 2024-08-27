package com.ecommerceapp.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecommerceapp.domain.viewmodel.AppViewModel
import com.ecommerceapp.adapters.ChilditemAdapter
import com.ecommerceapp.adapters.ImageAdapter
import com.ecommerceapp.adapters.SpecialOfferAdapter
import com.ecommerceapp.adapters.SpecialOfferAdapter2
import com.ecommerceapp.databinding.FragmentAllBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllFragment : Fragment() {
    private var _binding: FragmentAllBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var specialOfferAdapter: SpecialOfferAdapter
    private lateinit var specialOfferAdapter2: SpecialOfferAdapter2


    private val appViewModel: AppViewModel by viewModels()

    private lateinit var navController: NavController
    private lateinit var adapter: ChilditemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        imageAdapter = ImageAdapter(arrayListOf(), requireContext())
        binding.circleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.circleRecyclerView.adapter = imageAdapter
        binding.circleRecyclerView.setHasFixedSize(true)

        adapter = ChilditemAdapter(arrayListOf(), requireContext())
        binding.mainRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)//LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.mainRecyclerView.adapter = adapter
        binding.mainRecyclerView.setHasFixedSize(true)
        //this line for 3rd adapter
       specialOfferAdapter =SpecialOfferAdapter(arrayListOf(), requireContext())
        binding.grideRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.grideRecyclerView.adapter = specialOfferAdapter
        binding.grideRecyclerView.setHasFixedSize(true)

        //this line for 4rth adapter
        specialOfferAdapter2 =SpecialOfferAdapter2(arrayListOf(), requireContext())
        binding.gride2RecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, true)
        binding.gride2RecyclerView.adapter = specialOfferAdapter2
        binding.gride2RecyclerView.setHasFixedSize(true)






        appViewModel.gridProducts2.observe(viewLifecycleOwner) { grideproduct2 ->
            specialOfferAdapter2.updateList(grideproduct2)

        }


        appViewModel.images.observe(viewLifecycleOwner) { images ->
            imageAdapter.updateList(images)

        }


        appViewModel.products.observe(viewLifecycleOwner) { products ->
            adapter.updateList(products)

        }
        appViewModel.gridProducts.observe(viewLifecycleOwner) { grideproduct ->
            specialOfferAdapter.updateList(grideproduct)

        }


        appViewModel.errorMessage.observe(viewLifecycleOwner) {
            // Handle the error message, if needed
        }

        // Add dummy products to Firestore and fetch them
        appViewModel.getProductData()
        appViewModel.getImageData()
        appViewModel.getGridProductData()
        appViewModel.getGridProductData2()
    }

    companion object {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}