package com.ecommerceapp.presentation.adapters

import android.content.Context

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class ColorSpinnerAdapter(context: Context, private val colors: List<Pair<String, Int>>) :
    ArrayAdapter<Pair<String, Int>>(context, android.R.layout.simple_spinner_item, colors) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
        view.text = colors[position].first
        view.setBackgroundColor(colors[position].second)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
        view.text = colors[position].first
        view.setBackgroundColor(colors[position].second)
        return view
    }
}
