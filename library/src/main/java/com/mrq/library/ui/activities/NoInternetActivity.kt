package com.mrq.library.ui.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import com.mrq.library.base.BaseActivity
import com.mrq.library.databinding.ActivityNoInternetBinding

open class NoInternetActivity : BaseActivity() {

    private lateinit var binding: ActivityNoInternetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoInternetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        hideSystemUI(this, binding.open)
        binding.open.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
        }
        binding.close.setOnClickListener { finishAffinity() }
    }

    override fun onBackPressed() {

    }

}