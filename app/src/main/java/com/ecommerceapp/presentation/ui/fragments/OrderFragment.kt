package com.ecommerceapp.presentation.ui.fragments


import android.graphics.Color
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ecommerceapp.R
import com.ecommerceapp.databinding.FragmentOrderBinding
import com.flask.colorpicker.OnColorSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val args: OrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve the passed item model from arguments
        val passedItem = args.itemModel
        Log.d("OrderFragment", "Received item: $passedItem")

        // Bind the data to the views
        if (passedItem != null) {
            binding.parentTv.text = passedItem.productName
        }
        binding.textView8.text = "$${passedItem?.productPrice}"  // Display product price
        // Load image using Glide (or any image loading library)
        Glide.with(this)
            .load(passedItem?.productImage)  // URL for the product image
            .into(binding.imageView)

        // Optionally, you can set a fallback/default image if the URL is invalid or empty
        Glide.with(this)
            .load(passedItem?.productImage)
            .error(R.drawable.ic_placeholder)  // Provide a placeholder image
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Clean up binding when the fragment's view is destroyed
    }
}

/*
class OrderFragment : Fragment() {
    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        // Access views using binding
        val selectColorButton: Button = binding.selectColorButton
        val shirtImageView: ImageView = binding.shirtImageView

        // Set up the color picker dialog
        selectColorButton.setOnClickListener {
            ColorPickerDialog.newBuilder()
                .setDialogId(0)
                .setAllowCustom(true)  // Allow custom colors
                .setShowAlphaSlider(true)  // Show alpha slider
                .setColor(Color.RED)  // Default color
                .setPresets(intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA))  // Color presets
                .setCustomColor(Color.BLACK, Color.RED)  // Custom color default range
                .setOnColorSelectedListener(object : OnColorSelectedListener {
                    override fun onColorSelected(color: Int) {
                        // Change the color of the shirt image
                        shirtImageView.setColorFilter(color)
                    }
                })
                .build()
                .show(childFragmentManager, "color_picker")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
