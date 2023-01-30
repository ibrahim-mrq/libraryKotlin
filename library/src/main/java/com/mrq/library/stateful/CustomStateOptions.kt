package com.mrq.library.stateful

import android.view.View
import androidx.annotation.DrawableRes
import java.io.Serializable

/**
 * create by Ibrahim Mrq
 * 10/June/2021
 */

class CustomStateOptions : Serializable {
    @DrawableRes
    var imageRes = 0
        private set
    var isLoading = false
        private set
    var message: String? = null
        private set
    var buttonText: String? = null
        private set
    var clickListener: View.OnClickListener? = null
        private set

    fun image(@DrawableRes `val`: Int): CustomStateOptions {
        imageRes = `val`
        return this
    }

    fun loading(): CustomStateOptions {
        isLoading = true
        return this
    }

    fun message(`val`: String?): CustomStateOptions {
        message = `val`
        return this
    }

    fun buttonText(`val`: String?): CustomStateOptions {
        buttonText = `val`
        return this
    }

    fun buttonClickListener(`val`: View.OnClickListener?): CustomStateOptions {
        clickListener = `val`
        return this
    }
}