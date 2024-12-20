package com.bacancy.samplebaseapp.payWithStripe

import android.content.Context
import android.util.Log
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe

class StripeInitializer(private val context: Context) {
    fun initialize() {
        try {
            PaymentConfiguration.init(
                context,
                StripeConfig.publishableKey
            )

            Stripe(
                context,
                StripeConfig.publishableKey,
                StripeConfig.accountId
            )
        } catch (e: Exception) {
            Log.e("StripeInitializer", "Error initializing Stripe: ${e.message}")
            // Handle initialization error
        }
    }
}