package com.bacancy.samplebaseapp.payWithStripe

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bacancy.samplebaseapp.databinding.ActivityCheckoutBinding
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.stripe.android.PaymentConfiguration
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference


class CheckoutActivity : AppCompatActivity() {

    private lateinit var paymentConfiguration: PaymentConfiguration
    private lateinit var binding: ActivityCheckoutBinding
    private val backendUrl = "http://127.0.0.1:4242/"
    private val httpClient = OkHttpClient()
    private lateinit var paymentIntentClientSecret: String
    private lateinit var paymentLauncher: PaymentLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PaymentConfiguration.init(this, "pk_live_EpWPqGv8W7mAN4WkxcSV1chb")
        paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        val paymentsClient = Wallet.getPaymentsClient(
            this,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION) // Use ENVIRONMENT_PRODUCTION in live
                .build()
        )

        // Check if Google Pay is available
        val isReadyToPayRequest = IsReadyToPayRequest.fromJson(getGooglePayConfig().toString())
        paymentsClient.isReadyToPay(isReadyToPayRequest)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.googlePayPaymentButton.visibility = View.VISIBLE
                } else {
                    binding.googlePayPaymentButton.visibility = View.GONE
                }
            }

        /* paymentLauncher = PaymentLauncher.Companion.create(
             this,
             StripeConfig.publishableKey,
             StripeConfig.accountId,
             ::onPaymentResult
         )

         startCheckout()*/
        //binding.googlePayPaymentButton.isEnabled = false
        val paymentMethods: JSONArray = JSONArray().put(getSamplePaymentMethod())
        binding.googlePayPaymentButton.initialize(
            ButtonOptions.newBuilder()
                .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                .setButtonType(ButtonConstants.ButtonType.PAY)
                .setCornerRadius(100)
                .setAllowedPaymentMethods(paymentMethods.toString())
                .build())
        val googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Production,
                merchantCountryCode = "US",
                merchantName = "Widget Store"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        binding.googlePayPaymentButton.setOnClickListener {
            // launch `GooglePayLauncher` to confirm a Payment Intent
            googlePayLauncher.presentForPaymentIntent("NEED THIS FROM BACKEND")
        }
    }

    val paymentDataRequest = PaymentDataRequest.fromJson(getGooglePayConfig().toString())
    val googlePayLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val paymentData = PaymentData.getFromIntent(result.data!!)
            val token = paymentData?.paymentMethodToken?.token
            // Send token to Stripe
        }
    }


    private fun getSamplePaymentMethod(): JSONObject? {
        try {
            return JSONObject("{ \"type\": \"CARD\", \"parameters\": { \"allowedAuthMethods\": [\"PAN_ONLY\", \"CRYPTOGRAM_3DS\"], \"allowedCardNetworks\": [\"VISA\", \"MASTERCARD\", \"RUPAY\"] } }")
        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
    }

    private fun onGooglePayReady(isReady: Boolean) {
        // implemented below
        Log.d("TAG", "@@@onGooglePayReady: $isReady")
        if(isReady){
            binding.googlePayPaymentButton.isEnabled = true
        }
    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        // implemented below
        Log.d("TAG", "@@@onGooglePayResult: ${result.toString()}")
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
        /*val payButton: Button = findViewById(R.id.payButton)
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
        }*/
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

    private fun getGooglePayConfig(): JSONObject{
        return JSONObject().apply {
            put("environment", "PRODUCTION") // Use "TEST" for test
            put("apiVersion", 2)
            put("apiVersionMinor", 0)
            put("allowedPaymentMethods", JSONArray().apply {
                put(JSONObject().apply {
                    put("type", "CARD")
                    put("parameters", JSONObject().apply {
                        put("allowedAuthMethods", JSONArray().apply {
                            put("PAN_ONLY")
                            put("CRYPTOGRAM_3DS")
                        })
                        put("allowedCardNetworks", JSONArray().apply {
                            put("AMEX")
                            put("DISCOVER")
                            put("MASTERCARD")
                            put("VISA")
                        })
                    })
                    put("tokenizationSpecification", JSONObject().apply {
                        put("type", "PAYMENT_GATEWAY")
                        put("parameters", JSONObject().apply {
                            put("gateway", "stripe")
                            put("stripe:publishableKey", paymentConfiguration.publishableKey)
                            put("stripe:version", "2024-12-30")
                        })
                    })
                })
            })
        }
    }
}