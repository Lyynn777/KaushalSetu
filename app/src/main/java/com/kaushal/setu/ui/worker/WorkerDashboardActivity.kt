package com.kaushal.setu.ui.worker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.kaushal.setu.ui.common.BaseActivity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.R
import com.kaushal.setu.databinding.ActivityWorkerDashboardBinding
import com.kaushal.setu.ui.auth.LoginActivity
import com.kaushal.setu.ui.common.HireRequestAdapter
import com.kaushal.setu.ui.common.LanguageSettingsActivity
import com.kaushal.setu.ui.common.ServiceCardAdapter
import com.kaushal.setu.utils.confirmDialog
import com.kaushal.setu.utils.hide
import com.kaushal.setu.utils.show
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.AuthViewModel
import androidx.recyclerview.widget.GridLayoutManager
import com.kaushal.setu.viewmodel.WorkerViewModel
import com.kaushal.setu.ui.common.PortfolioAdapter
class WorkerDashboardActivity : BaseActivity() {
    private lateinit var b: ActivityWorkerDashboardBinding
    private val vm: WorkerViewModel by viewModels()
    private val authVm: AuthViewModel by viewModels()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private lateinit var serviceAdapter: ServiceCardAdapter
    private lateinit var requestAdapter: HireRequestAdapter
    private lateinit var portfolioAdapter: PortfolioAdapter
    private val pickPortfolio = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { vm.uploadPortfolioImage(uid, it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWorkerDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupAdapters()
        setupObservers()
        setupClicks()
        refresh()
        if (intent.getBooleanExtra("NEW_USER", false))
            startActivity(Intent(this, ProfileSetupActivity::class.java))
    }

    override fun onResume() { super.onResume(); refresh() }

    private fun refresh() {
        vm.loadProfile(uid)
        vm.loadServices(uid)
        vm.loadRequests(uid)
    }

    private fun setupAdapters() {
        serviceAdapter = ServiceCardAdapter(
            workerView = true,
            onDelete = { sid ->
                confirmDialog(
                    getString(R.string.delete_service_title),
                    getString(R.string.delete_service_msg)
                ) { vm.deleteService(uid, sid) }
            },
            onEdit = { service ->
                startActivity(
                    Intent(this, AddServiceActivity::class.java)
                        .putExtra("SERVICE_TO_EDIT", service)
                )
            }
        )
        b.rvServices.layoutManager = LinearLayoutManager(this)
        b.rvServices.adapter = serviceAdapter

        requestAdapter = HireRequestAdapter { rid, status -> vm.updateRequestStatus(rid, status) }
        b.rvRequests.layoutManager = LinearLayoutManager(this)
        b.rvRequests.adapter = requestAdapter

        portfolioAdapter = PortfolioAdapter()
        b.rvPortfolio.layoutManager = GridLayoutManager(this, 3)
        b.rvPortfolio.adapter = portfolioAdapter
    }

    private fun setupObservers() {
        vm.profile.observe(this) { p ->
            p ?: return@observe
            b.tvName.text = p.name
            b.tvCategory.text = p.skillCategory
            b.tvLocation.text = "📍 ${p.location}"
            b.tvExp.text = getString(R.string.years_exp, p.yearsOfExperience)
            b.tvRating.text = String.format("%.1f ★", p.averageRating)
            b.tvPortfolioCount.text = getString(R.string.photos_count, p.portfolioImages.size)
            b.tvPortfolioCount.text = getString(R.string.photos_count, p.portfolioImages.size)
            portfolioAdapter.submitList(p.portfolioImages)
            if (p.portfolioImages.isEmpty()) {
                b.tvNoPortfolio.visibility = View.VISIBLE
                b.rvPortfolio.visibility   = View.GONE
            } else {
                b.tvNoPortfolio.visibility = View.GONE
                b.rvPortfolio.visibility   = View.VISIBLE
            }
            b.chipAvailability.text = if (p.isAvailable) getString(R.string.status_available) else getString(R.string.status_busy)
            b.chipAvailability.setChipBackgroundColorResource(if (p.isAvailable) R.color.colorAvailable else R.color.colorBusy)
            if (p.profileImageUrl.isNotEmpty())
                Glide.with(this).load(p.profileImageUrl).circleCrop().into(b.ivAvatar)
        }

        vm.services.observe(this) { list ->
            serviceAdapter.submitList(list)
            if (list.isEmpty()) b.tvNoServices.show() else b.tvNoServices.hide()
        }

        vm.requests.observe(this) { list ->
            requestAdapter.submitList(list)
            val pending = list.count { it.status == "pending" }
            b.tvPendingCount.text = getString(R.string.pending_count, pending)
            if (list.isEmpty()) b.tvNoRequests.show() else b.tvNoRequests.hide()
        }

        vm.ui.observe(this) { state ->
            when (state) {
                is WorkerViewModel.UiState.Loading -> b.progressBar.show()
                is WorkerViewModel.UiState.Success -> { b.progressBar.hide(); toast(state.msg); refresh() }
                is WorkerViewModel.UiState.Error   -> { b.progressBar.hide(); toast(state.msg) }
                else -> b.progressBar.hide()
            }
        }

        authVm.state.observe(this) { if (it is AuthViewModel.AuthState.LoggedOut) {
            startActivity(Intent(this, LoginActivity::class.java)); finish()
        }}
    }

    private fun setupClicks() {
        b.btnEditProfile.setOnClickListener { startActivity(Intent(this, ProfileSetupActivity::class.java)) }
        b.btnAddService.setOnClickListener  { startActivity(Intent(this, AddServiceActivity::class.java)) }
        b.btnAddPortfolio.setOnClickListener { pickPortfolio.launch("image/*") }
        b.btnShareCard.setOnClickListener   { startActivity(Intent(this, ProfileCardActivity::class.java)) }
        b.btnLanguage.setOnClickListener    { startActivity(Intent(this, LanguageSettingsActivity::class.java)) }
        b.btnLogout.setOnClickListener      { confirmDialog(getString(R.string.logout_confirm_title), getString(R.string.logout_confirm_msg), getString(R.string.logout)) { authVm.logout() } }
        b.swipeRefresh.setOnRefreshListener { refresh(); b.swipeRefresh.isRefreshing = false }
    }
}