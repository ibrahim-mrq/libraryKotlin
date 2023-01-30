package com.mrq.library.base

import android.util.Patterns
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.mrq.library.R
import com.mrq.library.helpers.Utils

open class BaseFragment : Fragment(), Utils {

    fun isValidEmail(editText: EditText): Boolean {
        return if (Patterns.EMAIL_ADDRESS.matcher(editText.text.toString()).matches()) {
            editText.background(R.drawable.shape_edit_text_error)
            true
        } else {
            editText.background(R.drawable.shape_edit_text)
            editText.error = getString(R.string.invalid_email)
            false
        }
    }

    fun isNotEmpty(list: ArrayList<EditText>): ArrayList<Boolean> {
        val isBlank = ArrayList<Boolean>()
        list.forEach { editText ->
            if (editText.text.toString().isBlank()) {
                editText.background(R.drawable.shape_edit_text)
                isBlank.add(true)
            } else {
                editText.background(R.drawable.shape_edit_text_error)
                editText.error = getString(R.string.blank_field)
                isBlank.add(false)
            }
        }
        return isBlank
    }

    fun isNotEmpty(editText: EditText): Boolean {
        return if (editText.text.toString().isBlank()) {
            editText.background(R.drawable.shape_edit_text)
            true
        } else {
            editText.background(R.drawable.shape_edit_text_error)
            editText.error = getString(R.string.blank_field)
            false
        }
    }

    fun isConfirmPassword(password: EditText, confirmPass: EditText): Boolean {
        return if (password.text.toString() == confirmPass.text.toString()) {
            password.background(R.drawable.shape_edit_text)
            confirmPass.background(R.drawable.shape_edit_text)
            true
        } else {
            password.background(R.drawable.shape_edit_text_error)
            confirmPass.background(R.drawable.shape_edit_text_error)
            password.error = getString(R.string.password_not_match)
            confirmPass.error = getString(R.string.password_not_match)
            false
        }
    }

    fun isTextMoreThan(editText: EditText, charNumber: Int): Boolean {
        return if (editText.text.toString().length >= charNumber) {
            editText.background(R.drawable.shape_edit_text)
            true
        } else {
            editText.background(R.drawable.shape_edit_text_error)
            editText.error
            "${getString(R.string.password_must_than)} $charNumber ${getString(R.string.characters)}"
            false
        }
    }

}