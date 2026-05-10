package com.kaushal.setu

import android.app.Application
import android.content.Context
import com.kaushal.setu.utils.LocaleHelper

class KaushalSetuApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}