package com.pharos.aalamjobsemployer.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.pharos.aalamjobsemployer.R
import com.pharos.aalamjobsemployer.data.network.Resource
import com.pharos.aalamjobsemployer.ui.auth.login.LoginFragment
import com.pharos.aalamjobsemployer.ui.base.BaseFragment
const val CHOOSE_IMAGE_REQUEST = 456
fun <A : Activity> Activity.startNewActivity(activity: Class<A>){
    Intent(this, activity).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }
}

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

fun View.snackbar(msg: String, action: (() -> Unit)? = null){
    val snackbar = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
    action?.let {
        snackbar.setAction(context.getString(R.string.retry)){
            it()
        }
    }
    snackbar.show()
}

fun Fragment.handleApiError(
    failure: Resource.Failure,
    retry: (() -> Unit)? = null
){
    when {
        failure.isNetworkError -> requireView().snackbar(
            getString(R.string.check_internet_connect),
            retry
        )
        failure.errorCode == 401 -> {
            if (this is LoginFragment) {
                requireView().snackbar(getString(R.string.incorrect_email_pwd))
            } else {
                (this as BaseFragment<*, *, *>).logout()
            }
        }
        else -> {
            val error = failure.errorBody?.string().toString()
            requireView().snackbar(error)
        }
    }
}

fun hideSoftKeyboard(activity: Activity) {
    val inputMethodManager: InputMethodManager = activity.getSystemService(
        Activity.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    if (inputMethodManager.isAcceptingText) {
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken,
            0
        )
    }
}