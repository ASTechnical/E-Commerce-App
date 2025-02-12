package com.ecommerceapp.presentation.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.ecommerceapp.R
import com.ecommerceapp.models.ItemModel
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class HomeFragmentDirections private constructor() {
  private data class ActionHomeFragmentToCatogeryFragment(
    public val itemModel: ItemModel? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_homeFragment_to_catogeryFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java)) {
          result.putParcelable("itemModel", this.itemModel as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
          result.putSerializable("itemModel", this.itemModel as Serializable?)
        }
        return result
      }
  }

  private data class ActionHomeFragmentToOrderFragment(
    public val itemModel: ItemModel? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_homeFragment_to_orderFragment

    public override val arguments: Bundle
      @Suppress("CAST_NEVER_SUCCEEDS")
      get() {
        val result = Bundle()
        if (Parcelable::class.java.isAssignableFrom(ItemModel::class.java)) {
          result.putParcelable("itemModel", this.itemModel as Parcelable?)
        } else if (Serializable::class.java.isAssignableFrom(ItemModel::class.java)) {
          result.putSerializable("itemModel", this.itemModel as Serializable?)
        }
        return result
      }
  }

  public companion object {
    public fun actionHomeFragmentToCatogeryFragment(itemModel: ItemModel? = null): NavDirections =
        ActionHomeFragmentToCatogeryFragment(itemModel)

    public fun actionHomeFragmentToProfileFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_profileFragment)

    public fun actionHomeFragmentToOrderFragment(itemModel: ItemModel? = null): NavDirections =
        ActionHomeFragmentToOrderFragment(itemModel)
  }
}
