package com.bacancy.samplebaseapp

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.UiModeManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.viewpager2.widget.ViewPager2
import com.bacancy.samplebaseapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private var isDarkTheme: Boolean = false
    private val mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

    private val titles =
        listOf("Title 5", "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 1")
    private val images = listOf(
        R.drawable.images5,
        R.drawable.images1,
        R.drawable.images2,
        R.drawable.images3,
        R.drawable.image4,
        R.drawable.images5,
        R.drawable.images1
    )

    private val singlePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            // Handle the returned Uri
            Log.d("TAG", "onActivityResult: $uri")
            binding.ivSingleImage.setImageURI(uri)
        }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //makeManualCrash()

        trackScreenView()
        trackCustomEvent()

        setThemeDeviceDefault()

        binding.apply {

            btnToggleTheme.setOnClickListener {
                toggleTheme()
            }

            btnPickSingleImage.setOnClickListener {
                trackEvent("pick_single_image")
                handlePermissions()
            }

            val adapter = OnboardingAdapter(images)
            introViewPager.adapter = adapter

            introViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                        if (introViewPager.currentItem == 0)
                            introViewPager.setCurrentItem(images.size - 2, false)
                        else if (introViewPager.currentItem == images.size - 1)
                            introViewPager.setCurrentItem(1, false)
                    }
                }
            })

            TabLayoutMediator(intoTabLayout, introViewPager) { tab, position ->
                //To hide the first and last dots
                if (position == 0 || position == images.size - 1) {
                    tab.view.visibility = android.view.View.GONE
                } else {
                    tab.view.visibility = android.view.View.VISIBLE
                }

            }.attach()

            introViewPager.setCurrentItem(1, false)
        }
    }

    private fun setThemeDeviceDefault() {
        val uiManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        isDarkTheme = uiManager.nightMode == UiModeManager.MODE_NIGHT_YES
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            uiManager.setApplicationNightMode(uiManager.nightMode)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    private fun toggleTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val newNightMode = if (isDarkTheme) UiModeManager.MODE_NIGHT_NO else UiModeManager.MODE_NIGHT_YES
            val uiManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
            uiManager.setApplicationNightMode(newNightMode)
        } else {
            val newNightMode = if (isDarkTheme) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
            AppCompatDelegate.setDefaultNightMode(newNightMode)
        }
        isDarkTheme = !isDarkTheme
    }

    private fun trackEvent(eventName: String){
        mFirebaseAnalytics.logEvent(eventName, null)
    }

    private fun trackScreenView() {
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, "MainActivity")
        })
    }

    private fun trackCustomEvent() {
        mFirebaseAnalytics.setUserProperty("user_type", "test_user_1")
    }

    private fun makeManualCrash() {
        throw RuntimeException("Test Crash 2")
    }

    // Register ActivityResult handler
    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            // Handle permission requests results
            if(results[READ_MEDIA_IMAGES] == true || results[READ_MEDIA_VIDEO] == true || results[READ_MEDIA_VISUAL_USER_SELECTED] == true) {
                singlePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

    private fun handlePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (ContextCompat.checkSelfPermission(this, READ_MEDIA_IMAGES) == PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,READ_MEDIA_VIDEO) == PERMISSION_GRANTED)) {
            // Full access on Android 13 (API level 33) or higher
            singlePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                this,
                READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED
        ) {
            // Partial access on Android 14 (API level 34) or higher
            singlePhotoLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else if (ContextCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {
            // Full access up to Android 12 (API level 32)
        } else {
            // Access denied
            // Permission request logic
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(
                    arrayOf(
                        READ_MEDIA_IMAGES,
                        READ_MEDIA_VIDEO,
                        READ_MEDIA_VISUAL_USER_SELECTED
                    )
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }
    }

}