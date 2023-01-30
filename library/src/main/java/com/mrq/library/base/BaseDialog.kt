package com.mrq.library.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.mrq.library.R
import kotlin.Exception

open class BaseDialog<T>() : DialogFragment() where T : ViewDataBinding {

    private var layoutRes: Int = 0
    private var style: Int = R.style.DialogAnimation
    private var onBind: (binding: T) -> Unit = { _ -> }
    private var onCancelListener: () -> Unit = {}

    constructor(
        @LayoutRes layoutRes: Int,
        onBind: (binding: T) -> Unit,
        onCancelListener: () -> Unit = {}
    ) : this() {
        this.layoutRes = layoutRes
        this.onBind = onBind
        this.onCancelListener = onCancelListener
    }

    constructor(
        @LayoutRes layoutRes: Int,
        @StyleRes style: Int,
        onBind: (binding: T) -> Unit,
        onCancelListener: () -> Unit = {}
    ) : this() {
        this.layoutRes = layoutRes
        this.style = style
        this.onBind = onBind
        this.onCancelListener = onCancelListener
    }

    lateinit var binding: ViewDataBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun onBackPressed() {
                onCancelListener()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.setOnCancelListener {
                onCancelListener()
            }
            it.setOnDismissListener {
                onCancelListener()
            }
            it.window?.setLayout(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            it.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.window!!.setWindowAnimations(style)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
            binding.root
        } catch (e: Exception) {
            Log.e(
                "BaseDialogException",
                "Inflating Error, You had forget to convert your layout to data binding layout"
            )
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            onBind(binding as T)
        } catch (e: Exception) {
            Log.e(
                "BaseDialogException",
                "Casting error, please make sure you're passing the right binding class",
            )
        }
    }
}