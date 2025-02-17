package com.ecommerceapp.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemModel(
val productName: String = "",
val productImage: String = "",
val productRate: Double = 0.0,
val productPrice: Double = 0.0
):Parcelable