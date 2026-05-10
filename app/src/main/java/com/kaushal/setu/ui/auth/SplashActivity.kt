package com.kaushal.setu.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kaushal.setu.databinding.ActivitySplashBinding
import com.kaushal.setu.ui.customer.CustomerDashboardActivity
import com.kaushal.setu.ui.worker.WorkerDashboardActivity
import com.kaushal.setu.viewmodel.AuthViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var b: ActivitySplashBinding
    private val vm: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(b.root)

        vm.state.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Success -> {
                    val dest = if (state.user.userType == "worker")
                        WorkerDashboardActivity::class.java
                    else CustomerDashboardActivity::class.java
                    startActivity(Intent(this, dest))
                    finish()
                }
                is AuthViewModel.AuthState.LoggedOut -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
                else -> {}
            }
        }
        vm.checkSession()
    }
}