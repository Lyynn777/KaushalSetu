package com.kaushal.setu.ui.customer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.kaushal.setu.R
import com.kaushal.setu.databinding.ActivityCustomerDashboardBinding
import com.kaushal.setu.ui.auth.LoginActivity
import com.kaushal.setu.ui.common.LanguageSettingsActivity
import com.kaushal.setu.ui.common.WorkerCardAdapter
import com.kaushal.setu.utils.confirmDialog
import com.kaushal.setu.utils.hide
import com.kaushal.setu.utils.show
import com.kaushal.setu.viewmodel.AuthViewModel
import com.kaushal.setu.viewmodel.WorkerViewModel

class CustomerDashboardActivity : AppCompatActivity() {
    private lateinit var b: ActivityCustomerDashboardBinding
    private val vm: WorkerViewModel by viewModels()
    private val authVm: AuthViewModel by viewModels()
    private lateinit var adapter: WorkerCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityCustomerDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)

        adapter = WorkerCardAdapter { worker ->
            startActivity(Intent(this, WorkerDetailActivity::class.java).putExtra("UID", worker.uid))
        }
        b.rvWorkers.layoutManager = GridLayoutManager(this, 2)
        b.rvWorkers.adapter = adapter

        vm.workers.observe(this) { list ->
            adapter.submitList(list)
            b.tvCount.text = getString(R.string.workers_found, list.size)
            if (list.isEmpty()) b.tvEmpty.show() else b.tvEmpty.hide()
        }

        vm.ui.observe(this) { state ->
            b.progressBar.visibility = if (state is WorkerViewModel.UiState.Loading) View.VISIBLE else View.GONE
        }

        authVm.state.observe(this) { if (it is AuthViewModel.AuthState.LoggedOut) {
            startActivity(Intent(this, LoginActivity::class.java)); finish()
        }}

        // Category chips
        b.chipAll.setOnClickListener        { vm.loadAllWorkers() }
        b.chipElectrician.setOnClickListener { vm.searchWorkers(category = "Electrician") }
        b.chipPlumber.setOnClickListener     { vm.searchWorkers(category = "Plumber") }
        b.chipCarpenter.setOnClickListener   { vm.searchWorkers(category = "Carpenter") }
        b.chipPainter.setOnClickListener     { vm.searchWorkers(category = "Painter") }
        b.chipMechanic.setOnClickListener    { vm.searchWorkers(category = "Mechanic") }

        b.btnSearch.setOnClickListener   { startActivity(Intent(this, SearchActivity::class.java)) }
        b.btnLanguage.setOnClickListener { startActivity(Intent(this, LanguageSettingsActivity::class.java)) }
        b.btnLogout.setOnClickListener   { confirmDialog(getString(R.string.logout_confirm_title), getString(R.string.logout_confirm_msg), getString(R.string.logout)) { authVm.logout() } }
        b.swipeRefresh.setOnRefreshListener { vm.loadAllWorkers(); b.swipeRefresh.isRefreshing = false }

        vm.loadAllWorkers()
    }

    override fun onResume() { super.onResume(); vm.loadAllWorkers() }
}