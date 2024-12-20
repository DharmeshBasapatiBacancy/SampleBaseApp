package com.bacancy.samplebaseapp.payWithStripe

import com.bacancy.samplebaseapp.BuildConfig

object StripeConfig {
    private const val DUMMY_KEY = "dummy_key" // Fallback key for development

    val publishableKey: String
        get() = try {
            BuildConfig.STRIPE_PUBLISHABLE_KEY.takeIf { it.isNotBlank() } ?: DUMMY_KEY
        } catch (e: Exception) {
            DUMMY_KEY
        }

    val accountId: String
        get() = try {
            BuildConfig.STRIPE_ACCOUNT_ID.takeIf { it.isNotBlank() } ?: DUMMY_KEY
        } catch (e: Exception) {
            DUMMY_KEY
        }

    // Helper method to check if we're using test keys
    fun isTestMode(): Boolean = publishableKey.startsWith("pk_test_")
}