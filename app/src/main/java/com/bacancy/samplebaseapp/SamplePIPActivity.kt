package com.bacancy.samplebaseapp

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bacancy.samplebaseapp.background_syncing.NotificationService
import com.bacancy.samplebaseapp.databinding.ActivitySamplePipActivityBinding
import java.io.IOException

class SamplePIPActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySamplePipActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySamplePipActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestExactAlarmPermission()
        }

        startNotificationService()
    }

    private fun requestExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM), 1001)
            }
        }
    }

    private fun startNotificationService() {
        val serviceIntent = Intent(this, NotificationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    override fun onResume() {
        super.onResume()
        playVideo()
    }

    private fun playVideo() {
        binding.apply {
            runCatching {
                // Build the Uri for the video in the raw folder
                val rawId = resources.getIdentifier("sample_video", "raw", packageName)
                val videoUri = Uri.parse("android.resource://" + packageName + "/" + rawId)
                videoView.setVideoURI(videoUri)
                val mediaController = MediaController(this@SamplePIPActivity)
                mediaController.setAnchorView(videoView)  // Anchor the controller to the video view
                videoView.setMediaController(mediaController)
                videoView.start()
            }.onFailure {
                it.printStackTrace()
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