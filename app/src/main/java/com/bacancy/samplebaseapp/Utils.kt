package com.bacancy.samplebaseapp

import android.content.Context
import android.widget.Toast

object Utils {

    fun Context.showToast(message: String){
        // Show toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}