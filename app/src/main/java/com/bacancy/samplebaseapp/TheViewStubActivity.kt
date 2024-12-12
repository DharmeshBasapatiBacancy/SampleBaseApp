package com.bacancy.samplebaseapp

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bacancy.samplebaseapp.databinding.ActivityTheViewStubBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TheViewStubActivity : AppCompatActivity() {
    private var errorView: View? = null
    private var loadingView: View? = null
    private lateinit var binding: ActivityTheViewStubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTheViewStubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
        NetworkHelper.isNetworkConnected.observe(this, Observer { isConnected ->
            lifecycleScope.launch(Dispatchers.Main){
                if (isConnected) {
                    showLoading()
                } else {
                    showError()
                }
            }
        })
    }

    private fun setupListeners() {
        binding.btnLoadProgressLayout.setOnClickListener {
            showLoading()
        }

        binding.btnLoadErrorLayout.setOnClickListener {
            showError()
        }
    }

    private fun showLoading() {
        if (loadingView == null) {
            loadingView = findViewById<ViewStub>(R.id.vsProgressLayout).inflate()
        }
        loadingView?.visibility = View.VISIBLE
        errorView?.visibility = View.GONE
    }

    private fun showError() {
        if (errorView == null) {
            errorView = findViewById<ViewStub>(R.id.vsErrorLayout).inflate()
        }
        loadingView?.visibility = View.GONE
        errorView?.visibility = View.VISIBLE
    }
}