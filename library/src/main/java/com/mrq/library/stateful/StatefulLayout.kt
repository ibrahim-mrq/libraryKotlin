package com.mrq.library.stateful

import android.content.Context
import kotlin.jvm.JvmOverloads
import android.view.animation.Animation
import android.view.LayoutInflater
import com.mrq.library.R
import android.text.TextUtils
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.AnimRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * create by Ibrahim Mrq
 * 10/June/2021
 */

class StatefulLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs, 0) {
    /**
     * Indicates whether to place the animation on state changes
     */
    var isAnimationEnabled: Boolean

    /**
     * Animation started begin of state change
     */
    var inAnimation: Animation

    /**
     * Animation started end of state change
     */
    var outAnimation: Animation

    /**
     * to synchronize transition animations when animation duration shorter then request of state change
     */
    private var animCounter = 0
    private var content: View? = null
    private var stContainer: LinearLayout? = null
    private var stProgress: ProgressBar? = null
    private var stImage: ImageView? = null
    private var stMessage: TextView? = null
    private var stButton: Button? = null

    fun setInAnimation(@AnimRes anim: Int) {
        inAnimation = anim(anim)
    }

    fun setOutAnimation(@AnimRes anim: Int) {
        outAnimation = anim(anim)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        check(childCount <= 1) { MSG_ONE_CHILD }
        if (isInEditMode) return  // hide state views in designer
        orientation = VERTICAL
        content = getChildAt(0) // assume first child as content
        LayoutInflater.from(context).inflate(R.layout.custom_stf_template, this, true)
        stContainer = findViewById<View>(R.id.stContainer) as LinearLayout
        stProgress = findViewById<View>(R.id.stProgress) as ProgressBar
        stImage = findViewById<View>(R.id.stImage) as ImageView
        stMessage = findViewById<View>(R.id.stMessage) as TextView
        stButton = findViewById<View>(R.id.stButton) as Button
    }

    // TODO : Container Background ↓
    fun setContainerBackground(@ColorInt color: Int) {
        stContainer!!.setBackgroundColor(color)
    }

    fun setContainerBackground(color: String?) {
        stContainer!!.setBackgroundColor(Color.parseColor(color))
    }

    fun setContainerBackgroundResource(@DrawableRes color: Int) {
        stContainer!!.setBackgroundResource(color)
    }

    // TODO : content ↓
    fun showContent() {
        if (isAnimationEnabled) {
            stContainer!!.clearAnimation()
            content!!.clearAnimation()
            val animCounterCopy = ++animCounter
            if (stContainer!!.visibility == VISIBLE) {
                outAnimation.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounter != animCounterCopy) return
                        stContainer!!.visibility = GONE
                        content!!.visibility = VISIBLE
                        content!!.startAnimation(inAnimation)
                    }
                })
                stContainer!!.startAnimation(outAnimation)
            }
        } else {
            stContainer!!.visibility = GONE
            content!!.visibility = VISIBLE
        }
    }

    // TODO : loading ↓
    @JvmOverloads
    fun showLoading(@StringRes resId: Int = R.string.stfLoadingMessage) {
        showLoading(str(resId))
    }

    fun showLoading(message: String?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .loading()
        )
    }

    // TODO : empty ↓
    @JvmOverloads
    fun showEmpty(@StringRes resId: Int = R.string.stfEmptyMessage) {
        showEmpty(str(resId))
    }

    fun showEmpty(@StringRes resId: Int, @DrawableRes photo: Int) {
        showEmpty(str(resId), photo)
    }

    fun showEmpty(message: String?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty)
        )
    }

    fun showEmpty(message: String?, @DrawableRes photo: Int) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(photo)
        )
    }

    fun showEmpty(message: String?, @DrawableRes photo: Int, clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(photo)
                .buttonClickListener(clickListener)
        )
    }

    fun showEmpty(message: String?, clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_empty)
                .buttonClickListener(clickListener)
        )
    }

    fun showEmpty(clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(str(R.string.stfEmptyMessage))
                .image(R.drawable.stf_ic_empty)
                .buttonClickListener(clickListener)
        )
    }

    // TODO : error ↓
    fun showError(clickListener: OnClickListener?) {
        showError(R.string.stfErrorMessage, R.drawable.stf_ic_error, clickListener)
    }

    fun showError(@StringRes resId: Int, @DrawableRes photo: Int, clickListener: OnClickListener?) {
        showError(str(resId), photo, clickListener)
    }

    fun showError(message: String?, clickListener: OnClickListener?) {
        showError(message, R.drawable.stf_ic_error, clickListener)
    }

    fun showError(message: String?, @DrawableRes photo: Int, clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(photo)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener)
        )
    }

    fun showError(
        message: String?,
        btnText: String?,
        @DrawableRes photo: Int,
        clickListener: OnClickListener?
    ) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(photo)
                .buttonText(btnText)
                .buttonClickListener(clickListener)
        )
    }

    // TODO : offline ↓
    fun showOffline(clickListener: OnClickListener?) {
        showOffline(R.string.stfOfflineMessage, clickListener)
    }

    fun showOffline(
        @StringRes resId: Int,
        @DrawableRes photo: Int,
        clickListener: OnClickListener?
    ) {
        showOffline(str(resId), photo, clickListener)
    }

    fun showOffline(@StringRes resId: Int, clickListener: OnClickListener?) {
        showOffline(str(resId), R.drawable.stf_ic_offline, clickListener)
    }

    fun showOffline(message: String?, clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(R.drawable.stf_ic_offline)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener)
        )
    }

    fun showOffline(message: String?, @DrawableRes photo: Int, clickListener: OnClickListener?) {
        showCustom(
            CustomStateOptions()
                .message(message)
                .image(photo)
                .buttonText(str(R.string.stfButtonText))
                .buttonClickListener(clickListener)
        )
    }

    // TODO : custom ↓
    fun showCustom(options: CustomStateOptions) {
        if (isAnimationEnabled) {
            stContainer!!.clearAnimation()
            content!!.clearAnimation()
            val animCounterCopy = ++animCounter
            if (stContainer!!.visibility == GONE) {
                outAnimation.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter) return
                        content!!.visibility = GONE
                        stContainer!!.visibility = VISIBLE
                        stContainer!!.startAnimation(inAnimation)
                    }
                })
                content!!.startAnimation(outAnimation)
                state(options)
            } else {
                outAnimation.setAnimationListener(object : CustomAnimationListener() {
                    override fun onAnimationEnd(animation: Animation) {
                        if (animCounterCopy != animCounter) return
                        state(options)
                        stContainer!!.startAnimation(inAnimation)
                    }
                })
                stContainer!!.startAnimation(outAnimation)
            }
        } else {
            content!!.visibility = GONE
            stContainer!!.visibility = VISIBLE
            state(options)
        }
    }

    // TODO : helper methods ↓
    private fun state(options: CustomStateOptions) {
        if (!TextUtils.isEmpty(options.message)) {
            stMessage!!.visibility = VISIBLE
            stMessage!!.text = options.message
        } else {
            stMessage!!.visibility = GONE
        }
        if (options.isLoading) {
            stProgress!!.visibility = VISIBLE
            stImage!!.visibility = GONE
            stButton!!.visibility = GONE
        } else {
            stProgress!!.visibility = GONE
            if (options.imageRes != 0) {
                stImage!!.visibility = VISIBLE
                stImage!!.setImageResource(options.imageRes)
            } else {
                stImage!!.visibility = GONE
            }
            if (options.clickListener != null) {
                stButton!!.visibility = VISIBLE
                stButton!!.setOnClickListener(options.clickListener)
                if (!TextUtils.isEmpty(options.buttonText)) {
                    stButton!!.text = options.buttonText
                }
            } else {
                stButton!!.visibility = GONE
            }
        }
    }

    private fun str(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    private fun anim(@AnimRes resId: Int): Animation {
        return AnimationUtils.loadAnimation(context, resId)
    }

    companion object {
        private const val MSG_ONE_CHILD = "StatefulLayout must have one child!"
        private const val DEFAULT_ANIM_ENABLED = true
        private const val DEFAULT_IN_ANIM = android.R.anim.fade_in
        private const val DEFAULT_OUT_ANIM = android.R.anim.fade_out
    }

    init {
        val array = context.theme.obtainStyledAttributes(attrs, R.styleable.stfStatefulLayout, 0, 0)
        isAnimationEnabled = array.getBoolean(
            R.styleable.stfStatefulLayout_stfAnimationEnabled,
            DEFAULT_ANIM_ENABLED
        )
        inAnimation =
            anim(array.getResourceId(R.styleable.stfStatefulLayout_stfInAnimation, DEFAULT_IN_ANIM))
        outAnimation = anim(
            array.getResourceId(
                R.styleable.stfStatefulLayout_stfOutAnimation,
                DEFAULT_OUT_ANIM
            )
        )
        array.recycle()
    }
}