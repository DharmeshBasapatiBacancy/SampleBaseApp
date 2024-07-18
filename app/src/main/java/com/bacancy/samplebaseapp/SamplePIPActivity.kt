package com.bacancy.samplebaseapp

import android.app.PictureInPictureParams
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.bacancy.samplebaseapp.databinding.ActivitySamplePipActivityBinding
import java.io.IOException

class SamplePIPActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySamplePipActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySamplePipActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        playVideo()
    }

    private fun playVideo() {
        binding.apply {
            try {
                // Build the Uri for the video in the raw folder
                val rawId = resources.getIdentifier("sample_video", "raw", packageName)
                val videoUri = Uri.parse("android.resource://" + packageName + "/" + rawId)
                videoView.setVideoURI(videoUri)
                val mediaController = MediaController(this@SamplePIPActivity)
                mediaController.setAnchorView(videoView)  // Anchor the controller to the video view
                videoView.setMediaController(mediaController)
                videoView.start()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(binding.videoView.width, binding.videoView.height)
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterPipMode()
        }
    }
}