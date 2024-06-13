package com.bacancy.samplebaseapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bacancy.samplebaseapp.databinding.RowItemViewpagerBinding

class OnboardingAdapter(private val images: List<Int>) : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {

    class ViewHolder(val binding: RowItemViewpagerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        RowItemViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.apply {
                //Bind views with some data here
                val actualPosition = position % images.size
                imgBackground.setImageResource(images[actualPosition])
                tvTitle.text = "Title ${actualPosition}"
            }
        }
    }

    override fun getItemCount() = images.size
}