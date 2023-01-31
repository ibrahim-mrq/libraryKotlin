package com.mrq.library.base

import android.content.Intent
import android.util.Patterns
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.mrq.library.R
import com.mrq.library.helpers.Utils
import com.mrq.library.network.ConnectivityObserver
import com.mrq.library.network.NetworkConnectivityObserver
import com.mrq.library.ui.activities.NoInternetActivity
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

open class BaseActivity : AppCompatActivity(), Utils {

    private val connectivityObserver: NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }

    private fun connectivity() {
        lifecycleScope.launch {
            connectivityObserver.observe().collect {
                when (it) {
                    ConnectivityObserver.Status.Unavailable -> {
                        startActivity(Intent(this@BaseActivity, NoInternetActivity::class.java))
                    }
                    ConnectivityObserver.Status.Losing -> {
                        startActivity(Intent(this@BaseActivity, NoInternetActivity::class.java))
                    }
                    ConnectivityObserver.Status.Lost -> {
                        startActivity(Intent(this@BaseActivity, NoInternetActivity::class.java))
                    }
                    ConnectivityObserver.Status.Available -> {
                        if (this@BaseActivity.javaClass.simpleName.equals("NoInternetActivity")) {
                            super.onBackPressed()
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        connectivity()
    }

    override fun onStop() {
        super.onStop()
        connectivityObserver.unregisterNetworkCallback()
    }

    fun isValidEmail(editText: EditText): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
            editText.background(R.drawable.shape_edit_text_error)
            true
        } else {
            editText.background(R.drawable.shape_edit_text)
            editText.error = getString(R.string.invalid_email)
            false
        }
    }

    fun isNotEmpty(list: ArrayList<EditText>): ArrayList<Boolean> {
        val isBlank = ArrayList<Boolean>()
        list.forEach { editText ->
            if (editText.text.toString().isBlank()) {
                editText.background(R.drawable.shape_edit_text)
                isBlank.add(true)
            } else {
                editText.background(R.drawable.shape_edit_text_error)
                editText.error = getString(R.string.blank_field)
                isBlank.add(false)
            }
        }
        return isBlank
    }

    fun isNotEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isBlank()) {
            editText.background(R.drawable.shape_edit_text)
            true
        } else {
            editText.background(R.drawable.shape_edit_text_error)
            editText.error = getString(R.string.blank_field)
            false
        }
    }

    fun isConfirmPassword(password: EditText, confirmPass: EditText): Boolean {
        return if (password.text.toString() == confirmPass.text.toString()) {
            password.background(R.drawable.shape_edit_text)
            confirmPass.background(R.drawable.shape_edit_text)
            true
        } else {
            password.background(R.drawable.shape_edit_text_error)
            confirmPass.background(R.drawable.shape_edit_text_error)
            password.error = getString(R.string.password_not_match)
            confirmPass.error = getString(R.string.password_not_match)
            false
        }
    }

    fun isTextMoreThan(editText: EditText, charNumber: Int): Boolean {
        return if (editText.text.toString().length >= charNumber) {
            editText.background(R.drawable.shape_edit_text)
            true
        } else {
            editText.background(R.drawable.shape_edit_text_error)
            editText.error
            "${getString(R.string.password_must_than)} $charNumber ${getString(R.string.characters)}"
            false
        }
    }

//    fun isDebug(): Boolean {
//        return utils.isDebug()
//    }

//    fun printLog(msg: String) {
//        utils.printLog(msg)
//    }
//
//    fun printLog(tag: String, msg: String) {
//        utils.printLog(tag, msg)
//    }


}