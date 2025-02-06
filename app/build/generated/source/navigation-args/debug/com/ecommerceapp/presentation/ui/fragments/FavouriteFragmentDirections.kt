package com.ecommerceapp.presentation.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.ecommerceapp.R
import com.ecommerceapp.models.ItemModel
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class FavouriteFragmentDirections private constructor() {
  private data class ActionFavouriteFragmentToOrderFragment(
    public val itemModel: ItemModel? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_favouriteFragment_to_orderFragment

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
    public fun actionFavouriteFragmentToOrderFragment(itemModel: ItemModel? = null): NavDirections =
        ActionFavouriteFragmentToOrderFragment(itemModel)
  }
}
