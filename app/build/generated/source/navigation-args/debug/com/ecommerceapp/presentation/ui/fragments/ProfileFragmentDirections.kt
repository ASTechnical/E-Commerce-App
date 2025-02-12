package com.ecommerceapp.presentation.ui.fragments

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.ecommerceapp.R
import com.ecommerceapp.models.ItemModel
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class ProfileFragmentDirections private constructor() {
  private data class ActionProfileFragmentToHomeFragment(
    public val itemModel: ItemModel? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_profileFragment_to_homeFragment

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
    public fun actionProfileFragmentToHomeFragment(itemModel: ItemModel? = null): NavDirections =
        ActionProfileFragmentToHomeFragment(itemModel)
  }
}
