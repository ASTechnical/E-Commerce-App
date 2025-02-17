package com.ecommerceapp.domain.interfaces

import com.ecommerceapp.domain.models.ItemModel

interface OnClick {
    fun click(item: ItemModel)
}