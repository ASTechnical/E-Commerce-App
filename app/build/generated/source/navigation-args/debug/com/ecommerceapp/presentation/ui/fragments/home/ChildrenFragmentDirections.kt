package com.ecommerceapp.presentation.ui.fragments.home

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavDirections
import com.ecommerceapp.R
import com.ecommerceapp.domain.models.ItemModel
import java.io.Serializable
import kotlin.Int
import kotlin.Suppress

public class ChildrenFragmentDirections private constructor() {
  private data class ActionChildrenFragmentToOrderFragment(
    public val itemModel: ItemModel? = null,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_childrenFragment_to_orderFragment

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
    public fun actionChildrenFragmentToOrderFragment(itemModel: ItemModel? = null): NavDirections =
        ActionChildrenFragmentToOrderFragment(itemModel)
  }
}
