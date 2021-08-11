package com.sifat.androidxbiometric

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var biometricManager: BiometricManager
    private val tvMessage by lazy { findViewById<TextView>(R.id.tvMessage) }
    private val btnAuthenticate by lazy { findViewById<Button>(R.id.btnAuthenticate) }

    private val authCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            showMessage("Authentication successful")
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            showMessage("Unrecoverable error => $errString")
        }

        override fun onAuthenticationFailed() {
            showMessage("Could not recognise the user")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
    }

    // Step 2
    private fun authenticateUser() {
        biometricManager = BiometricManager.from(this)

        when (biometricManager.canAuthenticate(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS ->
                showBiometricPrompt()
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                showMessage("There is no suitable hardware")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                showMessage("The hardware is unavailable. Try again later")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                showMessage("No biometric or device credential is enrolled")
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED ->
                showMessage("A security vulnerability has been discovered with one or more hardware sensors")
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED ->
                showMessage("The specified options are incompatible with the current Android version")
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN ->
                showMessage("Unable to determine whether the user can authenticate")
        }
    }

    private fun showBiometricPrompt() {
        // Step 3
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("AndroidX Biometric")
            .setSubtitle("Authenticate user via Biometric")
            .setDescription("Please authenticate yourself here")
            .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            .setConfirmationRequired(true)
            .build()

        // Step 4
        val biometricPrompt = BiometricPrompt(this, authCallback)

        // Step 5
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setupView() {
        setContentView(R.layout.activity_main)
        btnAuthenticate.setOnClickListener { authenticateUser() }
    }

    private fun showMessage(message: String) {
        Log.d(TAG, "Message: $message")
        tvMessage.text = message
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
