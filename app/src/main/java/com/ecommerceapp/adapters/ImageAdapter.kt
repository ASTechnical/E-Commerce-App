package com.ecommerceapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ecommerceapp.R
import com.ecommerceapp.models.ImageItem
import de.hdodenhof.circleimageview.CircleImageView

class ImageAdapter(private var imageslist: ArrayList<ImageItem>, private val context: Context) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = imageslist[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return imageslist.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<ImageItem>) {
        imageslist.clear()
        imageslist.addAll(newList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val childItem: ImageView = itemView.findViewById(R.id.childItem)
        //private val itemName: TextView = itemView.findViewById(R.id.parentTv)
        //  private val itemPrice: TextView = itemView.findViewById(R.id.textView8)
        // private val itemRate: TextView = itemView.findViewById(R.id.textView3)

        fun bind(item: ImageItem) {
            // itemName.text = item.productName
            //  itemPrice.text = item.productPrice.toString()
            //   itemRate.text = item.productRate.toString()

            Glide.with(context)
                .load(item.images)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_placeholder))
                .into(childItem)
        }
    }



}
