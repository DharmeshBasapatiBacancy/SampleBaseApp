package com.bacancy.samplebaseapp

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bacancy.samplebaseapp.databinding.ActivityNetworkDetectBinding

class NetworkDetectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNetworkDetectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNetworkDetectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        NetworkHelper.isNetworkConnected.observe(this) { isConnected ->
            showNetworkStatus(isConnected)
        }
        binding.btnSeeViewStatus.setOnClickListener {
            startActivity(Intent(this, TheViewStubActivity::class.java))
        }

    }

    private fun showNetworkStatus(isConnected: Boolean) {
        val message = if (isConnected) "You are online" else "You are offline"
        val color = if (isConnected) Color.GREEN else Color.RED
        binding.tvNetworkStatus.text = message
        binding.tvNetworkStatus.setTextColor(color)
    }
}