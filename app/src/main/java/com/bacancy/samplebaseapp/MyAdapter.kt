package com.bacancy.samplebaseapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val data: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // ViewHolder class to hold your item views
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Bind data to view elements here
        val textView: TextView = itemView.findViewById(R.id.tvType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.your_item_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item
        // Bind data to view elements in holder based on item type
    }

    override fun getItemCount(): Int = data.size
}