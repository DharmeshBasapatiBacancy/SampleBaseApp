package com.bacancy.samplebaseapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.GridLayoutManager
import com.bacancy.samplebaseapp.Utils.showToast
import com.bacancy.samplebaseapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //authenticateFingerprint()

        binding.btnLoginWithFingerprint.setOnClickListener {
            authenticateFingerprint()
        }

        setupDynamicViews()
    }

    private fun setupDynamicViews() {
        val parentList =
            listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7")

        val firstListSize = minOf(2, parentList.size) // Ensure first list doesn't exceed parent size
        val firstList = parentList.subList(0, firstListSize)

        val secondListSize = minOf(3, parentList.size - firstListSize) // Consider remaining items
        val secondList = parentList.subList(firstListSize, firstListSize + secondListSize)

        val remainingList = parentList.subList(firstListSize + secondListSize, parentList.size)

        if (firstList.isNotEmpty()) {
            val layout1 = createDynamicLinearLayout(this, firstList)
            binding.lnrParent.addView(layout1)
        }

        if (secondList.isNotEmpty()) {
            val layout2 = createDynamicLinearLayout(this, secondList)
            binding.lnrParent.addView(layout2)
        }

        if (remainingList.isNotEmpty()) {
            val otherChunkedList = remainingList.chunked(3)//Change size according to your needs
            otherChunkedList.forEach {
                binding.lnrParent.addView(createDynamicLinearLayout(this, it))
            }
        }
    }

    private fun createDynamicLinearLayout(context: Context, itemNames: List<String>): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation =
            LinearLayout.HORIZONTAL // Change to HORIZONTAL for horizontal layout
        itemNames.forEachIndexed { index, s ->
            val rowItem = createDynamicRowItem(
                context,
                s,
                (index == 0 || index == itemNames.size - 1),
                index == 0,
                index == itemNames.size - 1
            )
            linearLayout.addView(rowItem)
            if (index != itemNames.size - 1) {
                linearLayout.addView(createDynamicDivider(context))
            }
        }

        return linearLayout
    }

    private fun createDynamicRowItem(
        context: Context,
        text: String,
        isFirstOrLastItem: Boolean,
        isFirstItem: Boolean,
        isLastItem: Boolean
    ): LinearLayout {
        val inflater = LayoutInflater.from(context)
        val rowItemLayout = inflater.inflate(R.layout.your_item_layout, null) as LinearLayout
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.weight = 1.0f
        rowItemLayout.layoutParams = layoutParams
        if (isFirstItem) {
            rowItemLayout.setHorizontalGravity(Gravity.START)
        } else if (isLastItem) {
            rowItemLayout.setHorizontalGravity(Gravity.END)
        } else {
            rowItemLayout.setHorizontalGravity(Gravity.CENTER)
        }

        val textView = rowItemLayout.findViewById<TextView>(R.id.tvType)

        textView.text = text

        return rowItemLayout
    }

    private fun createDynamicDivider(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val rowItemLayout = inflater.inflate(R.layout.custom_divider, null) as LinearLayout
        return rowItemLayout
    }

    private fun authenticateFingerprint() {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Handle authentication error (e.g., too many attempts)
                    showToast("Authentication Error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Fingerprint recognized, grant access to app
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                    // Your code to grant access to the app
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Fingerprint not recognized
                    showToast("Fingerprint Authentication Failed!")
                }
            })

        val promptForBiometricOnly = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Sample App")
            .setDescription("Use your fingerprint to continue")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setNegativeButtonText("Enter App PIN")
            .build()

        val promptForBiometricOrCredential = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Sample App")
            .setDescription("Confirm your phone screen lock pattern, PIN or password to continue.")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        //biometricPrompt.authenticate(promptForBiometricOnly)
        biometricPrompt.authenticate(promptForBiometricOrCredential)
    }
}