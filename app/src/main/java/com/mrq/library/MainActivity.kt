package com.mrq.library

import android.os.Bundle
import com.mrq.library.auth.Biometric
import com.mrq.library.auth.SocialMediaModel
import com.mrq.library.base.BaseActivity
import com.mrq.library.base.BaseSocialMedia
import com.mrq.library.databinding.ActivityMainBinding

class MainActivity : BaseSocialMedia() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
//        facebookSdkInitialize(this, "", "")

        binding.view.setOnClickListener {
//            val biometric = Biometric(this, true)
//            biometric.getInstance(binding.view) {
//                showAlert(this, "test", R.color.purple_200)
//            }
            facebookLoginRequest(
                this,
                "",
                "",
                object : SocialMediaResults<SocialMediaModel> {
                    override fun onSuccess(response: SocialMediaModel) {
                        showAlert(this@MainActivity, response.name!!, R.color.purple_200)
                    }

                    override fun onFailure(message: String) {
                        showAlert(this@MainActivity, message, R.color.purple_500)
                    }

                    override fun onException(message: String) {
                        showAlert(this@MainActivity, message, R.color.teal_200)
                    }
                })
//            dialogLogin(this)
        }


//        dialogLogin(this)
//        showAlert(this, "test", R.color.purple_200)
    }


}
