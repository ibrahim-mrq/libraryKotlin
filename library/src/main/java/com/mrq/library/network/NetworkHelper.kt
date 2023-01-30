@file:Suppress("DEPRECATION")

package com.mrq.library.network

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.mrq.library.R
import com.mrq.library.databinding.DialogAlertBinding
import com.mrq.library.base.BaseDialog

object NetworkHelper {

    private fun isConnected(context: Context): Boolean {
        val info =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
        return info != null && info.isConnected
    }

    fun isNetworkOnline(context: Context): Boolean {
        var isOnline = false
        if (isConnected(context)) {
            try {
                val manager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
                isOnline = (capabilities != null
                        && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return isOnline
    }

    fun dialogOpenWirelessSettings(context: Context) {
        lateinit var dialog: BaseDialog<DialogAlertBinding>
        dialog = BaseDialog(
            layoutRes = R.layout.dialog_alert,
            onBind = { binding ->
                with(binding) {
                    title.text = context.getString(R.string.no_internet_connection)
                    message.text = context.getString(R.string.do_open_internet_settings)
                    photo.setImageResource(R.drawable.stf_ic_offline)
                    cancel.setOnClickListener { dialog.dismiss() }
                    ok.setOnClickListener {
                        dialog.dismiss()
                        context.startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
                    }
                }
            })
        dialog.isCancelable = false
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    fun isGpsOpen(activity: Activity): Boolean {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun dialogOpenGpsSettings(context: Context) {
        lateinit var dialog: BaseDialog<DialogAlertBinding>
        dialog = BaseDialog(
            layoutRes = R.layout.dialog_alert,
            onBind = { binding ->
                with(binding) {
                    message.text = context.getString(R.string.open_gps_settings)
                    cancel.setOnClickListener { dialog.dismiss() }
                    ok.setOnClickListener {
                        dialog.dismiss()
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                }
            })
        dialog.isCancelable = false
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    fun returnServerException(context: Context, code: Int): String {
        val message = when (code) {
            401, 403 -> {
                context.getString(R.string.logged_from_another_device) + "$code"
            }
            404 -> {
                context.getString(R.string.not_found) + "$code"
            }
            429 -> {
                context.getString(R.string.many_request) + "$code"
            }
            500 -> {
                context.getString(R.string.server_is_broker) + "$code"
            }
            503 -> {
                context.getString(R.string.server_not_available) + "$code"
            }
            else -> {
                context.getString(R.string.error_unknown) + "$code"
            }
        }
        return message
    }

}