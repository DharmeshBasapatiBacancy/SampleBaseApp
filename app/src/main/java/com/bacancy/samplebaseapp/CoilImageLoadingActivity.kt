package com.bacancy.samplebaseapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.bacancy.samplebaseapp.databinding.ActivityCoilImageLoadingBinding

class CoilImageLoadingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCoilImageLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoilImageLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupImagesList()
    }

    private fun setupImagesList() {
        binding.rvImageView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        binding.rvImageView.adapter = ImageAdapter(this, IMAGES_URL_LIST)
    }

    companion object {
        const val IMAGE_URL = "https://www.w3schools.com/w3css/img_lights.jpg" //https://fastly.picsum.photos/id/17/2500/1667.jpg?hmac=HD-JrnNUZjFiP2UZQvWcKrgLoC_pc_ouUSWv8kHsJJY
        val IMAGES_URL_LIST = listOf(
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
            "https://www.w3schools.com/w3css/img_lights.jpg",
            "https://www.w3schools.com/w3css/img_mountains.jpg",
            "https://www.w3schools.com/w3css/img_forest.jpg",
        )
    }
}