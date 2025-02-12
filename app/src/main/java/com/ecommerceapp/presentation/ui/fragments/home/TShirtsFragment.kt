package com.ecommerceapp.presentation.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.ecommerceapp.databinding.FragmentTShirtsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TShirtsFragment : Fragment() {
    private var _binding: FragmentTShirtsBinding? = null
    private val binding get() = _binding!!

    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var spinner3: Spinner


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTShirtsBinding.inflate(inflater, container, false)

        // Initialize spinners
        spinner1 = binding.spinner1
        spinner2 = binding.spinner2
        spinner3 = binding.spinner3

        // Prepare lists for the Spinners
        val list1 = arrayOf("Option 1", "Option 2", "Option 3")
        val list2 = arrayOf("Item A", "Item B", "Item C")
        val list3 = arrayOf("Choice X", "Choice Y", "Choice Z")

        // Set up the adapters for the Spinners
        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list1)
        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list2)
        val adapter3 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list3)

        // Set the adapters for each Spinner
        spinner1.adapter = adapter1
        spinner2.adapter = adapter2
        spinner3.adapter = adapter3

        // Optionally, set a listener to handle item selection (if needed)
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedOption = list1[position]
                // Handle the selected option here
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = list2[position]
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedChoice = list3[position]
                // Handle the selected choice here
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        // Return the root view from the binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding to avoid memory leaks
        _binding = null
    }
}
