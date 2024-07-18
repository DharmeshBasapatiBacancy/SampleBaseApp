package com.bacancy.samplebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        }

        setupDynamicGridList()
    }

    private fun setupDynamicGridList() {
        val dynamicGridList = DynamicGridList(this, binding.lnrParent)
        dynamicGridList.setupDynamicViews(listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7"))
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