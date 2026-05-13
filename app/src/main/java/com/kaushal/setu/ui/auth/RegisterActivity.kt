package com.kaushal.setu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.kaushal.setu.ui.common.BaseActivity
import androidx.appcompat.app.AppCompatActivity
import com.kaushal.setu.R
import com.kaushal.setu.databinding.ActivityRegisterBinding
import com.kaushal.setu.ui.customer.CustomerDashboardActivity
import com.kaushal.setu.ui.worker.WorkerDashboardActivity
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.AuthViewModel

class RegisterActivity : BaseActivity() {
    private lateinit var b: ActivityRegisterBinding
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)

        vm.state.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> setLoading(true)
                is AuthViewModel.AuthState.Success -> {
                    setLoading(false)
                    val dest = if (state.user.userType == "worker")
                        WorkerDashboardActivity::class.java
                    else CustomerDashboardActivity::class.java
                    startActivity(Intent(this, dest).putExtra("NEW_USER", true))
                    finish()
                }
                is AuthViewModel.AuthState.Error -> { setLoading(false); toast(state.msg) }
                else -> setLoading(false)
            }
        }

        b.btnRegister.setOnClickListener {
            val name     = b.etName.text.toString().trim()
            val phone    = b.etPhone.text.toString().trim()
            val email    = b.etEmail.text.toString().trim()
            val pass     = b.etPassword.text.toString().trim()
            val confirm  = b.etConfirmPassword.text.toString().trim()
            val userType = if (b.rbWorker.isChecked) "worker" else "customer"
            if (!validate(name, phone, email, pass, confirm)) return@setOnClickListener
            vm.register(email, pass, name, phone, userType)
        }

        b.tvLogin.setOnClickListener { finish() }
    }

    private fun validate(name: String, phone: String, email: String, pass: String, confirm: String): Boolean {
        var ok = true
        val req = getString(R.string.error_required)
        if (name.isEmpty())    { b.tilName.error = req; ok = false } else b.tilName.error = null
        if (phone.isEmpty())   { b.tilPhone.error = req; ok = false } else b.tilPhone.error = null
        if (email.isEmpty())   { b.tilEmail.error = req; ok = false } else b.tilEmail.error = null
        if (pass.length < 6)   { b.tilPassword.error = getString(R.string.error_password_short); ok = false } else b.tilPassword.error = null
        if (pass != confirm)   { b.tilConfirmPassword.error = getString(R.string.error_password_mismatch); ok = false } else b.tilConfirmPassword.error = null
        return ok
    }

    private fun setLoading(loading: Boolean) {
        b.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        b.btnRegister.isEnabled = !loading
    }
}