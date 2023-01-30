package com.mrq.library.auth

import android.content.Context
import android.util.Log
import android.view.View
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.lang.Exception
import java.util.concurrent.Executor

class Biometric(private val context: Context, val hasUserBiometric: Boolean) {

    private var executor: Executor? = null
    private var biometricPrompt: BiometricPrompt? = null
    private var promptInfo: PromptInfo? = null

    fun getInstance(view: View, onSucceeded: () -> Unit) {
        checkBiometric(view)
        intBiometric(onSucceeded)
    }

    fun onClick() {
        try {
            biometricPrompt!!.authenticate(promptInfo!!)
        } catch (ex: Exception) {
        }
    }

    private fun intBiometric(onSucceeded: () -> Unit) {
        executor = ContextCompat.getMainExecutor(context)
        biometricPrompt = BiometricPrompt((context as FragmentActivity), executor!!,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        Log.e("Authentication error", errString.toString())
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    if (hasUserBiometric) {
                        onSucceeded()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.e("Authentication failed", "Authentication failed")
                }
            })

        promptInfo = PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun checkBiometric(view: View) {
        if (hasUserBiometric) {
            val biometricManager = BiometricManager.from(context)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED,
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED,
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                    view.visibility = View.GONE
                }
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    view.visibility = View.VISIBLE
                }
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {}
            }
        } else {
            view.visibility = View.GONE
        }
    }

}