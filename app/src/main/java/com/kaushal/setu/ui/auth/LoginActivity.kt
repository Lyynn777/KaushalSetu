package com.kaushal.setu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.kaushal.setu.ui.common.BaseActivity
import androidx.appcompat.app.AppCompatActivity
import com.kaushal.setu.databinding.ActivityLoginBinding
import com.kaushal.setu.ui.customer.CustomerDashboardActivity
import com.kaushal.setu.ui.worker.WorkerDashboardActivity
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.AuthViewModel

class LoginActivity : BaseActivity() {
    private lateinit var b: ActivityLoginBinding
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(b.root)

        vm.state.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> setLoading(true)
                is AuthViewModel.AuthState.Success -> {
                    setLoading(false)
                    val dest = if (state.user.userType == "worker")
                        WorkerDashboardActivity::class.java
                    else CustomerDashboardActivity::class.java
                    startActivity(Intent(this, dest))
                    finish()
                }
                is AuthViewModel.AuthState.Error -> { setLoading(false); toast(state.msg) }
                else -> setLoading(false)
            }
        }

        b.btnLogin.setOnClickListener {
            val email = b.etEmail.text.toString().trim()
            val pass  = b.etPassword.text.toString().trim()
            if (!validate(email, pass)) return@setOnClickListener
            vm.login(email, pass)
        }

        b.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validate(email: String, pass: String): Boolean {
        var ok = true
        if (email.isEmpty()) { b.tilEmail.error = getString(com.kaushal.setu.R.string.error_required); ok = false }
        else b.tilEmail.error = null
        if (pass.isEmpty()) { b.tilPassword.error = getString(com.kaushal.setu.R.string.error_required); ok = false }
        else b.tilPassword.error = null
        return ok
    }

    private fun setLoading(loading: Boolean) {
        b.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        b.btnLogin.isEnabled = !loading
    }
}