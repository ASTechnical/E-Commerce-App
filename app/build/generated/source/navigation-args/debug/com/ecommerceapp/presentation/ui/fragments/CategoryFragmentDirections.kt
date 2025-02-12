package com.ecommerceapp.presentation.ui.fragments

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.ecommerceapp.R

public class CategoryFragmentDirections private constructor() {
  public companion object {
    public fun actionCatogeryFragmentToProductListFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_catogeryFragment_to_productListFragment)
  }
}
