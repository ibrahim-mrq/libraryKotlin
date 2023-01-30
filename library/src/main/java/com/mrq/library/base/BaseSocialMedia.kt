package com.mrq.library.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mrq.library.R
import com.mrq.library.auth.SocialMediaModel
import org.json.JSONException
import org.json.JSONObject

open class BaseSocialMedia : BaseActivity() {

    lateinit var googleLoginActivityForResult: ActivityResultLauncher<Intent>

    fun facebookLoginRequest(socialMediaResults: SocialMediaResults<SocialMediaModel>) {
        val callbackManager: CallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logOut()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    val request = GraphRequest.newMeRequest(
                        result.accessToken
                    ) { response: JSONObject?, _: GraphResponse? ->
                        try {
                            val id = response!!.getString("id")
                            val name = response.getString("name")
                            val email = response.getString("email")
                            socialMediaResults.onSuccess(
                                SocialMediaModel(
                                    id,
                                    name,
                                    email,
                                    1,
                                    0,
                                )
                            )
                        } catch (e: JSONException) {
                            socialMediaResults.onFailure(e.message.toString())
                        }
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id, name, email")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    socialMediaResults.onException(getString(R.string.task_cancelled))
                }

                override fun onError(error: FacebookException) {
                    socialMediaResults.onFailure(error.message.toString())
                }
            })
        LoginManager.getInstance()
            .logInWithReadPermissions(
                this,
                callbackManager,
                listOf("public_profile", "email")
            )
    }

    fun loginWithGoogle(context: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
//            .requestIdToken(context.getString(R.string.default_web_client_id))
            .build()
        val client: GoogleSignInClient = GoogleSignIn.getClient(context, gso)
        client.signOut()
        val signInIntent = client.signInIntent
        googleLoginActivityForResult.launch(signInIntent)
    }

    fun googleLoginForResult(result: SocialMediaResults<SocialMediaModel>) {
        googleLoginActivityForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { results: ActivityResult ->
                when (results.resultCode) {
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(results.data)
                        try {
                            val response = task.getResult(ApiException::class.java)
                            result.onSuccess(
                                SocialMediaModel(
                                    response.id,
                                    response.displayName,
                                    response.email,
                                    3,
                                    0
                                )
                            )
                        } catch (e: ApiException) {
                            e.printStackTrace()
                            result.onFailure(e.message.toString())
                        }
                    }
                    else -> {
                        result.onException(getString(R.string.task_cancelled))
                    }
                }
            }
    }

//    fun getDeviceToken(deviceToken: DeviceTokenInterface) {
//        FirebaseMessaging.getInstance().token
//            .addOnCompleteListener { tokenResult ->
//                if (tokenResult.isSuccessful) {
//                    deviceToken.tokenResult(tokenResult.result)
//                } else {
//                    deviceToken.tokenResult("")
//                }
//            }
//    }

    interface SocialMediaResults<T> {
        fun onSuccess(response: T)
        fun onFailure(message: String)
        fun onException(message: String)
    }

    interface DeviceTokenInterface {
        fun tokenResult(deviceToken: String)
    }

}

