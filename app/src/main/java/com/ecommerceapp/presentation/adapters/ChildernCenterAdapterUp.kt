package com.ecommerceapp.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ecommerceapp.R
import com.ecommerceapp.domain.models.ItemModel


class ChildernCenterAdapterUp (private var list: ArrayList<ItemModel>, private val context: Context) :
RecyclerView.Adapter<ChildernCenterAdapterUp.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.gride_item2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ItemModel>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val childItem: ImageView = itemView.findViewById(R.id.gride_imageView)
        private val itemName: TextView = itemView.findViewById(R.id.tvSpecialOffer)
        private val itemPrice: TextView = itemView.findViewById(R.id.tv3)
        //   private val itemRate: ImageView = itemView.findViewById(R.id.textView3)

        fun bind(item: ItemModel) {
            itemName.text = item.productName
            itemPrice.text = item.productPrice.toString()
            //     itemRate.text = item.productRate.toString()

            Glide.with(context)
                .load(item.productImage)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
                .into(childItem)
        }
    }
}