package com.kaushal.setu.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleHelper {
    private const val PREFS = "ks_prefs"
    private const val KEY   = "language"
    const val EN = "en"
    const val KN = "kn"

    fun onAttach(context: Context): Context = setLocale(context, getSaved(context))

    fun setLocale(context: Context, lang: String): Context {
        save(context, lang)
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.createConfigurationContext(config)
        else {
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            context
        }
    }

    fun getSaved(context: Context): String =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY, EN) ?: EN

    private fun save(context: Context, lang: String) =
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit().putString(KEY, lang).apply()
}