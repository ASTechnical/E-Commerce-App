package com.ecommerceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.ecommerceapp.R

class MyAdapter(
    private val items: List<String>,

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val hop: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.parent_item, parent, false)
        return ViewHolder(hop)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = items[position]
         //   ViewHolder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }





    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.parentTv)

        fun bind(item: String, position: Int) {
            textView.text = item
            itemView.setOnClickListener {


            }
        }
    }


}