package com.ecommerceapp.models


data class CategoriesModel(
    val name: String,
    val imageUrl: String,
    val isBestSeller: Boolean,
    val isSpecialOffer: Boolean
)
