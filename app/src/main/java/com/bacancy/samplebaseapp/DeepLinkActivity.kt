package com.bacancy.samplebaseapp

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bacancy.samplebaseapp.databinding.ActivityDeepLinkBinding

class DeepLinkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeepLinkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeepLinkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data: Uri? = intent.data
        data?.let {
            val contentId = it.getQueryParameter("id") ?: "Unknown"
            binding.tvProductDetail.text = "Opened Product ID - $contentId"
        }
    }
}