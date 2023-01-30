@file:Suppress("DEPRECATION")

package com.mrq.library.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.ImageViewCompat
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mrq.library.R
import com.mrq.library.base.BaseDialog
import com.mrq.library.databinding.DialogAlertBinding
import com.mrq.library.databinding.DialogListFilterBinding
import com.mrq.library.stateful.StatefulLayout
import com.mrq.library.ui.dapters.DialogFilterAdapter
import com.orhanobut.hawk.Hawk
import com.tapadoo.alerter.Alerter
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

interface Utils {

    // TODO : Alert
    fun showAlert(activity: Activity, text: String, @ColorRes color: Int) {
        Alerter.create(activity)
            .setTitle(activity.getString(R.string.alert))
            .setText(text)
            .setBackgroundColorRes(color)
            .setContentGravity(Gravity.START)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .setTextAppearance(R.style.alertTextStyle)
            .setTitleAppearance(R.style.alertTitleStyle)
            .show()
    }

    fun showAlertBackPressed(activity: Activity, text: String, @ColorRes color: Int): Alerter {
        return Alerter.create(activity)
            .setTitle(activity.getString(R.string.alert))
            .setText(text)
            .setBackgroundColorRes(color)
            .setContentGravity(Gravity.START)
            .enableSwipeToDismiss()
            .setDuration(2000)
            .hideIcon()
            .setTextAppearance(R.style.alertTextStyle)
            .setTitleAppearance(R.style.alertTitleStyle)
    }

    // TODO : Authentication
    fun logout(context: Activity) {
//        LocalData.deleteUser()
//        context.startActivity(Intent(context, SplashActivity::class.java))
//        context.finish()
    }

    fun dialogLogout(context: Activity) {
        dialog(
            context,
            context.getString(R.string.sure_logout),
            context.getString(R.string.logout)
        ) { dialog ->
            dialog.dismiss()
            logout(context)
        }
    }

    fun login(context: Activity) {
//        val intent = Intent(context, LoginActivity::class.java)
//        context.startActivity(intent)
    }

    fun dialogLogin(context: Activity) {
        dialog(
            context,
            context.getString(R.string.must_login),
            context.getString(R.string.log_in)
        ) { dialog ->
            dialog.dismiss()
            login(context)
        }
    }

    fun dialogDeleteAccount(
        context: Activity,
        onOkListener: (dialogs: BaseDialog<DialogAlertBinding>) -> Unit
    ) {
        dialog(
            context,
            context.getString(R.string.sure_delete_account),
            context.getString(R.string.delete_account),
        ) { dialog ->
            onOkListener(dialog)
        }
    }

    // TODO : Language
    fun changeAppLanguage(context: Activity, language: Language) {
        LocaleHelper.setLocale(context, language.value)
        Hawk.put(Constants.LANGUAGE_TYPE, language.value)
//        context.startActivity(Intent(context, SplashActivity::class.java))
        context.finish()
    }

    fun getAppLanguage(context: Activity): String? {
        return LocaleHelper.getLanguage(context)
    }

    fun dialogLanguage(context: Activity) {
        val list = ArrayList<String>()
        list.add(Language.ENGLISH.value)
        list.add(Language.ARABIC.value)
        val adapter = DialogFilterAdapter(context)
        adapter.setList(list)
        if (getAppLanguage(context).equals(Language.AR.value)) {
            adapter.setSelectedPosition(1)
        } else {
            adapter.setSelectedPosition(0)
        }
        lateinit var dialog: BaseDialog<DialogListFilterBinding>
        dialog = BaseDialog(
            layoutRes = R.layout.dialog_list_filter,
            onBind = { binding ->
                binding.title.text =
                    context.getString(R.string.select_language)
                binding.recyclerview.adapter = adapter
                binding.close.setOnClickListener { dialog.dismiss() }
                binding.apply.setOnClickListener {
                    dialog.dismiss()
                    val asd = adapter.getList()[adapter.getSelectedPosition()]
                    if (asd != Hawk.get(Constants.LANGUAGE, "")) {
                        if (adapter.getSelectedPosition() == 0) {
                            Hawk.put(Constants.LANGUAGE, Language.ENGLISH.value)
                            changeAppLanguage(context, Language.EN)
                        } else {
                            Hawk.put(Constants.LANGUAGE, Language.ARABIC.value)
                            changeAppLanguage(context, Language.AR)
                        }
                    }
                }
            },
            onCancelListener = { dialog.dismiss() }
        )
        dialog.isCancelable = false
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    // TODO :WebView
    @SuppressLint("SetJavaScriptEnabled")
    fun initWebView(webView: WebView, stateful: StatefulLayout) {
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.builtInZoomControls = false
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false

        stateful.showLoading()
        webView.visibility = View.INVISIBLE

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.e("RESPONSE", request!!.url.toString())
                view!!.loadUrl(request.url.toString())
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                stateful.showContent()
                webView.visibility = View.VISIBLE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                stateful.showError(error.toString(), null)
            }
        }
    }

    fun loadDataWithBaseURL(webView: WebView, htmlString: String, paddingPx: Int, textSizePx: Int) {
        val background = "background:transparent"
        val padding = "padding:" + paddingPx + "px"
        val fontSize = "font-size:" + textSizePx + "px"
        val verticalAlign = "vertical-align:middle"
        val htmlStyling =
            "<html><style>body{$background; $padding; $fontSize; $verticalAlign;}</style></html>"
        val result = "$htmlStyling<body>$htmlString</body>"
        webView.loadDataWithBaseURL(null, result, "text/html", "UTF-8", null)
    }

    // TODO : Dialogs
    fun dialog(
        context: Activity,
        message: String,
        ok: String,
        onOkListener: (dialog: BaseDialog<DialogAlertBinding>) -> Unit
    ) {
        lateinit var dialog: BaseDialog<DialogAlertBinding>
        dialog = BaseDialog(
            layoutRes = R.layout.dialog_alert,
            onBind = { binding ->
                binding.message.text = message
                binding.ok.text = ok
                binding.cancel.setOnClickListener { dialog.dismiss() }
                binding.ok.setOnClickListener { onOkListener(dialog) }
            },
            onCancelListener = { dialog.dismiss() }
        )
        dialog.isCancelable = false
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    fun dialog(context: Activity, message: String, ok: String) {
        lateinit var dialog: BaseDialog<DialogAlertBinding>
        dialog = BaseDialog(
            layoutRes = R.layout.dialog_alert,
            onBind = { binding ->
                binding.message.text = message
                binding.ok.text = ok
                binding.cancel.visibility = View.GONE
                binding.ok.setOnClickListener { dialog.dismiss() }
            },
            onCancelListener = { dialog.dismiss() }
        )
        dialog.isCancelable = false
        dialog.show((context as AppCompatActivity).supportFragmentManager, "")
    }

    // TODO : System
    fun hideSystemUI(activity: Activity, view: View) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val window: Window = activity.window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view)
                .addOnControllableInsetsChangedListener { controller: WindowInsetsControllerCompat, _: Int ->
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                    controller.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
        } else {
            activity.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun showSystemUI(activity: Activity, view: View) {
        val window: Window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, view).show(WindowInsetsCompat.Type.systemBars())
    }

    fun hideKeyboard(activity: Activity, view: View) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getKeyHash(context: Activity) {
        try {
            @SuppressLint("PackageManagerGetSignatures") val info =
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT)
                Log.e("response", "KeyHash = $keyHash")
            }
        } catch (ignored: PackageManager.NameNotFoundException) {
        } catch (ignored: NoSuchAlgorithmException) {
        }
    }

    fun startActivity(context: Activity, activity: Activity) {
        context.startActivity(Intent(context, activity::class.java))
    }

    fun TextView.setTextViewDrawableColor(color: Int) {
        for (drawable in this.compoundDrawables) {
            if (drawable != null) {
                drawable.colorFilter =
                    PorterDuffColorFilter(
                        ContextCompat.getColor(this.context, color),
                        PorterDuff.Mode.SRC_IN
                    )
            }
        }
    }

    fun TextView.setTextViewColor(color: Int) {
        this.setTextColor(ContextCompat.getColor(this.context, color))
    }

    fun TextView.fromHtml(text: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.text = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        } else {
            @Suppress("DEPRECATION")
            this.text = Html.fromHtml(text)
        }
    }

    fun EditText.addTextChangedListener(onTextChanged: (text: String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(c: CharSequence?, p0: Int, p1: Int, p2: Int) {}

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged(text.toString())
            }

            override fun afterTextChanged(text: Editable?) {}
        })
    }

    fun EditText.background(background: Int) {
        this.background = ContextCompat.getDrawable(this.context, background)
    }

    fun Button.background(background: Int) {
        this.background = ContextCompat.getDrawable(this.context, background)
    }

    fun ImageView.setTint(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(this.context, colorId)
        val colorStateList = ColorStateList.valueOf(color)
        ImageViewCompat.setImageTintList(this, colorStateList)
    }

    fun getText(editText: EditText): String {
        return editText.text.toString().trim()
    }

    fun getText(editText: TextView): String {
        return editText.text.toString().trim()
    }

    fun getColor(context: Activity, @ColorRes colorId: Int): Int {
        return ContextCompat.getColor(context, colorId)
    }

    fun getColor(color: String): Int {
        return Color.parseColor(color)
    }

    // TODO : Tab & ViewPager Layout
    fun initTabLayout(context: Context, viewpager: ViewPager2, tabLayout: TabLayout) {
        val adapter = ViewPagerAdapter((context as AppCompatActivity))
//        adapter.addFragment(HomeFragment(), "ONE")
        viewpager.adapter = adapter
        setTabLayoutDivider(tabLayout)
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = adapter.getTitle()[position]
        }.attach()
    }

    fun setTabLayoutDivider(tabLayout: TabLayout) {
        val root: View = tabLayout.getChildAt(0)
        if (root is LinearLayout) {
            root.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(getColor("#a4197d"))
            drawable.setSize(2, 2)
            root.dividerDrawable = drawable
        }
    }

    fun animationLogo(logo: View, delay: Long) {
        logo.y = 50f
        logo.visibility = View.VISIBLE
        val springAnimY = SpringAnimation(logo, DynamicAnimation.TRANSLATION_Y, 0f)
        springAnimY.setStartValue(50f)
        springAnimY.spring.dampingRatio = 0f
        springAnimY.spring.stiffness = SpringForce.STIFFNESS_VERY_LOW
        springAnimY.start()
        Handler(Looper.getMainLooper()).postDelayed({
            springAnimY.spring.dampingRatio = 1f
        }, delay)
    }

    // TODO : update App In Google App Store
    fun updateAvailability(context: Activity) {
        val appUpdateManager = AppUpdateManagerFactory.create(context)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                dialogUpdateApp(context)
            }
        }
    }

    private fun dialogUpdateApp(context: Activity) {
        dialog(context, "update", context.getString(R.string.update)) {
            val appUrl = "https://play.google.com/store/apps/details?id=${context.packageName}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(appUrl))
            context.startActivity(intent)
        }
    }

    fun printLog(msg: String) {
        Log.e(this.javaClass.simpleName, "printLog: $msg")
    }

    fun getZeroPrefix(minute: Int): String {
        return if (minute < 10)
            "0$minute"
        else minute.toString()
    }

}