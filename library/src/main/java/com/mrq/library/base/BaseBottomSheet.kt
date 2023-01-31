package com.mrq.library.base

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mrq.library.R

class BaseBottomSheet() : BottomSheetDialogFragment() {

    private lateinit var binding: ViewDataBinding
    private lateinit var _dialog: Dialog

    private var layoutRes: Int = 0
    private var animationStyle: Int = R.style.BottomSheetAnimation
    private var onBind: (dialog: Dialog, binding: ViewDataBinding) -> Unit = { _, _ -> }

    var bottomSheetBehavior = BottomSheetBehavior.STATE_EXPANDED

    constructor(
        @LayoutRes layoutRes: Int,
        onBind: (dialog: Dialog, binding: ViewDataBinding) -> Unit,
    ) : this() {
        this.layoutRes = layoutRes
        this.onBind = onBind
    }

    constructor(
        @LayoutRes layoutRes: Int,
        @StyleRes animationStyle: Int,
        onBind: (dialog: Dialog, binding: ViewDataBinding) -> Unit,
    ) : this() {
        this.layoutRes = layoutRes
        this.animationStyle = animationStyle
        this.onBind = onBind
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            if (layoutRes == 0) {
                dismiss()
                null
            } else {
                binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
                dialog?.setOnShowListener { dialog ->
                    val sheetDialog = dialog as BottomSheetDialog
                    val bottomSheet =
                        sheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                    val behavior = BottomSheetBehavior.from(bottomSheet)
                    behavior.state = bottomSheetBehavior
                    behavior.peekHeight = binding.root.height
                    behavior.isFitToContents = false
                    behavior.isDraggable = false
                    behavior.isHideable = true
                }
                _dialog = dialog!!
                _dialog.window!!.setWindowAnimations(animationStyle)
                binding.root
            }
        } catch (e: Exception) {
            dismiss()
            Log.e(
                "BaseBottomSheetException",
                "Inflating Error, You had forget to convert your layout to data binding layout"
            )
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            onBind(_dialog, binding)
        } catch (e: Exception) {
            Log.e(
                "BaseBottomSheetException",
                "Casting error, please make sure you're passing the right binding class",
            )
        }
    }

}