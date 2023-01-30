package com.mrq.library.base

import android.app.Application
import android.content.Context
import com.mrq.library.helpers.Language
import com.mrq.library.helpers.LocaleHelper.onAttach
import com.orhanobut.hawk.Hawk

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(baseContext).build()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(onAttach(base, Language.AR.value))
    }
}