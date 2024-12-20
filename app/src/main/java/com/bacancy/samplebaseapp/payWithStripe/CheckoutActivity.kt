package com.bacancy.samplebaseapp.payWithStripe

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bacancy.samplebaseapp.R
import com.bacancy.samplebaseapp.databinding.ActivityCheckoutBinding
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import com.stripe.android.view.CardInputWidget
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject
import java.lang.ref.WeakReference

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val backendUrl = "http://127.0.0.1:4242/"
    private val httpClient = OkHttpClient()
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        paymentLauncher = PaymentLauncher.Companion.create(
            this,
            StripeConfig.publishableKey,
            StripeConfig.accountId,
            ::onPaymentResult
        )

        startCheckout()
    }

    private fun displayAlert(
        activity: Activity,
        title: String,
        message: String,
    ) {
        runOnUiThread {
            val builder = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)

            builder.setPositiveButton("Ok", null)
            builder.create().show()
        }
    }

    private fun startCheckout() {
        val weakActivity = WeakReference<Activity>(this)
        // Create a PaymentIntent by calling your server's endpoint.
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestJson = """
      {
          "currency":"usd",
          "items": [
              {"id":"xl-tshirt"}
          ]
      }
      """
        val body = requestJson.toRequestBody(mediaType)
        val request = Request.Builder()
            .url(backendUrl + "create-payment-intent")
            .post(body)
            .build()
        httpClient.newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    weakActivity.get()?.let { activity ->
                        displayAlert(activity, "Failed to load page", "Error: $e")
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        weakActivity.get()?.let { activity ->
                            displayAlert(
                                activity,
                                "Failed to load page",
                                "Error: $response"
                            )
                        }
                    } else {
                        val responseData = response.body?.string()
                        val responseJson =
                            responseData?.let { JSONObject(it) } ?: JSONObject()

                        // For added security, our sample app gets the publishable key
                        // from the server.
                        paymentIntentClientSecret = responseJson.getString("clientSecret")
                    }
                }
            })

        // Hook up the pay button to the card widget and stripe instance
        val payButton: Button = findViewById(R.id.payButton)
        payButton.setOnClickListener {
            val cardInputWidget =
                findViewById<CardInputWidget>(R.id.cardInputWidget)
            cardInputWidget.paymentMethodCreateParams?.let { params ->
                if(this::paymentIntentClientSecret.isInitialized){
                    val confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret)
                    paymentLauncher.confirm(confirmParams)
                }
            }
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        var title = ""
        var message = ""
        when (paymentResult) {
            is PaymentResult.Completed -> {
                title = "Setup Completed"
            }
            is PaymentResult.Canceled -> {
                title = "Setup Canceled"
            }
            is PaymentResult.Failed -> {
                title = "Setup Failed"
                message = paymentResult.throwable.message!!
            }
        }
        displayAlert(this,title, message)
    }
}