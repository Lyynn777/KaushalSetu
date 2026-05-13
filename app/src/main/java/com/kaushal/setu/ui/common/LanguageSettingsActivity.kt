package com.kaushal.setu.ui.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaushal.setu.databinding.ActivityLanguageSettingsBinding
import com.kaushal.setu.ui.auth.SplashActivity
import com.kaushal.setu.utils.LocaleHelper
import com.kaushal.setu.ui.common.BaseActivity
class LanguageSettingsActivity : BaseActivity() {
    private lateinit var b: ActivityLanguageSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLanguageSettingsBinding.inflate(layoutInflater)
        setContentView(b.root)

        val current = LocaleHelper.getSaved(this)
        b.btnEnglish.alpha = if (current == LocaleHelper.EN) 1f else 0.5f
        b.btnKannada.alpha = if (current == LocaleHelper.KN) 1f else 0.5f
        b.tvCurrent.text = if (current == LocaleHelper.EN) "Current: English" else "ಪ್ರಸ್ತುತ: ಕನ್ನಡ"

        b.btnBack.setOnClickListener { finish() }
        b.btnEnglish.setOnClickListener { switchTo(LocaleHelper.EN) }
        b.btnKannada.setOnClickListener { switchTo(LocaleHelper.KN) }
    }

    private fun switchTo(lang: String) {
        LocaleHelper.setLocale(this, lang)
        startActivity(Intent(this, SplashActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
    }
}