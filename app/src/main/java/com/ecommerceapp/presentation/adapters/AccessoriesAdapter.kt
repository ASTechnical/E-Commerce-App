package com.ecommerceapp.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ecommerceapp.R
import com.ecommerceapp.domain.models.CategoriesModel

class AccessoriesAdapter(private var list: List<CategoriesModel>, private val context: Context) :
    RecyclerView.Adapter<AccessoriesAdapter.ViewHolder>() {

    // Called when the view holder is created for the first time
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_design_layout, parent, false)
        return ViewHolder(view)
    }

    // Called to bind data to the view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    // Returns the total number of items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Efficiently updates the list and notifies only the relevant changes
    fun updateList(newList: List<CategoriesModel>) {
        val diffCallback = AccessoriesDiffCallback(list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list = newList
        diffResult.dispatchUpdatesTo(this)  // Efficiently notify the changes
    }

    // ViewHolder that holds the views for each item
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val childItem: ImageView = itemView.findViewById(R.id.category_imageView)
        private val itemName: TextView = itemView.findViewById(R.id.categorySpecialOffer)

        // Binding the data to the views
        fun bind(item: CategoriesModel) {
            itemName.text = item.name

            // Loading the image with Glide, using the application context to avoid leaks
            Glide.with(context.applicationContext)
                .load(item.imageUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
                .into(childItem)
        }
    }

    // DiffUtil callback to calculate changes between the old and new list
    class AccessoriesDiffCallback(
        private val oldList: List<CategoriesModel>,
        private val newList: List<CategoriesModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        // Replace 'productId' with the actual unique identifier field in your AccessoriesModel
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].name == newList[newItemPosition].name
        }

        // Compare the contents of the items to check if the data is the same
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}