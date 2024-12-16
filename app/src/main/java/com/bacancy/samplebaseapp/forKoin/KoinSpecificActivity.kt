package com.bacancy.samplebaseapp.forKoin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bacancy.samplebaseapp.databinding.ActivityKoinSpecificBinding
import com.bacancy.samplebaseapp.silentUpdates.ApiWorker
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class KoinSpecificActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKoinSpecificBinding
    private val viewModel: UserViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKoinSpecificBinding.inflate(layoutInflater)
        setContentView(binding.root)
        scheduleApiWork()
    }

    private fun scheduleApiWork() {
        val newWorkRequest = OneTimeWorkRequestBuilder<ApiWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(applicationContext).enqueue(newWorkRequest)
    }
}