package com.ecommerceapp.presentation.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import com.ecommerceapp.models.ItemModel
import java.io.Serializable
import java.lang.UnsupportedOperationException
import kotlin.Suppress
import kotlin.jvm.JvmStatic

public data class CategoryFragmentArgs(
  public val itemModel: ItemModel? = null,
) : NavArgs {
  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toBundle(): Bundle {
    val result = Bundle()
    if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java)) {
      result.putParcelable("itemModel", this.itemModel as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
      result.putSerializable("itemModel", this.itemModel as Serializable?)
    }
    return result
  }

  @Suppress("CAST_NEVER_SUCCEEDS")
  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java)) {
      result.set("itemModel", this.itemModel as Parcelable?)
    } else if (Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
      result.set("itemModel", this.itemModel as Serializable?)
    }
    return result
  }

  public companion object {
    @JvmStatic
    @Suppress("DEPRECATION")
    public fun fromBundle(bundle: Bundle): CategoryFragmentArgs {
      bundle.setClassLoader(CategoryFragmentArgs::class.java.classLoader)
      val __itemModel : ItemModel?
      if (bundle.containsKey("itemModel")) {
        if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java) ||
            Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
          __itemModel = bundle.get("itemModel") as ItemModel?
        } else {
          throw UnsupportedOperationException(ItemModel::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __itemModel = null
      }
      return CategoryFragmentArgs(__itemModel)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): CategoryFragmentArgs {
      val __itemModel : ItemModel?
      if (savedStateHandle.contains("itemModel")) {
        if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java) ||
            Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
          __itemModel = savedStateHandle.get<ItemModel?>("itemModel")
        } else {
          throw UnsupportedOperationException(ItemModel::class.java.name +
              " must implement Parcelable or Serializable or must be an Enum.")
        }
      } else {
        __itemModel = null
      }
      return CategoryFragmentArgs(__itemModel)
    }
  }
}
