package com.bacancy.samplebaseapp.forKoin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bacancy.samplebaseapp.databinding.ActivityKoinSpecificBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class KoinSpecificActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKoinSpecificBinding
    private val viewModel: UserViewModel by viewModel<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKoinSpecificBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchUsers()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.users.collect { users ->
                Log.d("KoinActivity", "onCreate: $users")
            }
        }

    }
}