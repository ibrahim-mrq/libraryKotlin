package com.mrq.library.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.mrq.library.R


object AnimationHelper {

    var width = 0f
        get() {
            if (field == 0f) {
                field = Resources.getSystem().displayMetrics.widthPixels.toFloat() * 1.5f
            }
            return field
        }
        private set
    var height = 0f
        get() {
            if (field == 0f) {
                field = Resources.getSystem().displayMetrics.heightPixels.toFloat() * 1.5f
            }
            return field
        }
        private set

    @JvmOverloads
    fun makeViewVisibleWithBounceBottom(
        view: View,
        damping: Float = SpringForce.DAMPING_RATIO_LOW_BOUNCY,
        stiffness: Float = 200f,
        startValue: Float = height * -1f
    ) {
        view.visibility = View.INVISIBLE
        view.y = startValue
        view.visibility = View.VISIBLE
        val springAnim = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y, 0f)
        springAnim.setStartValue(startValue)
        springAnim.spring.dampingRatio = damping
        springAnim.spring.stiffness = stiffness
        Handler(Looper.myLooper()!!).postDelayed({ springAnim.start() }, 10)
    }

    @JvmOverloads
    fun makeViewVisibleWithBounceTop(
        view: View,
        damping: Float = SpringForce.DAMPING_RATIO_LOW_BOUNCY,
        stiffness: Float = 200f,
        startValue: Float = height
    ) {
        view.visibility = View.INVISIBLE
        view.y = startValue
        view.visibility = View.VISIBLE
        val springAnim = SpringAnimation(view, DynamicAnimation.TRANSLATION_Y, 0f)
        springAnim.setStartValue(startValue)
        springAnim.spring.dampingRatio = damping
        springAnim.spring.stiffness = stiffness
        Handler(Looper.myLooper()!!).postDelayed({ springAnim.start() }, 10)
    }

    @JvmOverloads
    fun makeViewVisibleWithBounceLeft(
        view: View,
        damping: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,
        stiffness: Float = 200f,
        startValue: Float = width * -1f
    ) {
        view.visibility = View.INVISIBLE
        view.x = startValue
        view.visibility = View.VISIBLE
        val springAnim = SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 0f)
        springAnim.setStartValue(startValue)
        springAnim.spring.dampingRatio = damping
        springAnim.spring.stiffness = stiffness
        Handler(Looper.myLooper()!!).postDelayed({ springAnim.start() }, 10)
    }

    @JvmOverloads
    fun makeViewVisibleWithBounceRight(
        view: View,
        damping: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY,
        stiffness: Float = 100f,
        startValue: Float = width
    ) {
        view.visibility = View.INVISIBLE
        view.x = startValue
        view.visibility = View.VISIBLE
        val springAnim = SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 0f)
        springAnim.setStartValue(startValue)
        springAnim.spring.dampingRatio = damping
        springAnim.spring.stiffness = stiffness
        Handler(Looper.myLooper()!!).postDelayed({ springAnim.start() }, 10)
    }

    fun arrowAnimation(view: View, viewVisibility: View, rotation: Float, visibility: Int) {
        view.animate()
            .rotation(rotation)
            .setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    viewVisibility.visibility = visibility
                }
            })
    }
}