package com.mrq.library

import android.os.Bundle
import com.mrq.library.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dialogLogin(this)
        showAlert(this, "test", R.color.purple_200)


    }
}