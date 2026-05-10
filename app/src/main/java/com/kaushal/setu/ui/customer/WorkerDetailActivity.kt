package com.kaushal.setu.ui.customer

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.R
import com.kaushal.setu.data.model.HireRequest
import com.kaushal.setu.data.model.Review
import com.kaushal.setu.databinding.ActivityWorkerDetailBinding
import com.kaushal.setu.ui.common.ReviewAdapter
import com.kaushal.setu.ui.common.ServiceCardAdapter
import com.kaushal.setu.utils.hide
import com.kaushal.setu.utils.show
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.AuthViewModel
import com.kaushal.setu.viewmodel.WorkerViewModel

class WorkerDetailActivity : AppCompatActivity() {
    private lateinit var b: ActivityWorkerDetailBinding
    private val vm: WorkerViewModel by viewModels()
    private val authVm: AuthViewModel by viewModels()
    private val currentUid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var workerUid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityWorkerDetailBinding.inflate(layoutInflater)
        setContentView(b.root)
        workerUid = intent.getStringExtra("UID") ?: ""

        val svcAdapter = ServiceCardAdapter(workerView = false)
        b.rvServices.layoutManager = LinearLayoutManager(this); b.rvServices.adapter = svcAdapter

        val revAdapter = ReviewAdapter()
        b.rvReviews.layoutManager = LinearLayoutManager(this); b.rvReviews.adapter = revAdapter

        vm.profile.observe(this) { p ->
            p ?: return@observe
            b.tvName.text = p.name; b.tvCategory.text = p.skillCategory
            b.tvLocation.text = "📍 ${p.location}"; b.tvExp.text = getString(R.string.years_exp, p.yearsOfExperience)
            b.tvBio.text = p.bio.ifEmpty { getString(R.string.no_bio) }
            b.ratingBar.rating = p.averageRating
            b.tvRating.text = "%.1f".format(p.averageRating)
            b.tvRatingCount.text = "(${p.totalRatings})"
            b.tvPortfolioCount.text = getString(R.string.portfolio_count, p.portfolioImages.size)
            b.chipAvailability.text = if (p.isAvailable) getString(R.string.status_available) else getString(R.string.status_busy)
            if (p.profileImageUrl.isNotEmpty())
                Glide.with(this).load(p.profileImageUrl).circleCrop().into(b.ivAvatar)
        }

        vm.services.observe(this) { list ->
            svcAdapter.submitList(list)
            if (list.isEmpty()) b.tvNoServices.show() else b.tvNoServices.hide()
        }

        vm.reviews.observe(this) { list ->
            revAdapter.submitList(list)
            if (list.isEmpty()) b.tvNoReviews.show() else b.tvNoReviews.hide()
        }

        authVm.user.observe(this) { user ->
            user?.let { b.etYourName.setText(it.name); b.etYourPhone.setText(it.phone) }
        }

        vm.ui.observe(this) { state ->
            when (state) {
                is WorkerViewModel.UiState.Loading -> b.progressBar.show()
                is WorkerViewModel.UiState.Success -> {
                    b.progressBar.hide(); toast(state.msg)
                    if ("Request" in state.msg) b.hireForm.hide()
                    if ("Review" in state.msg)  { b.reviewForm.hide(); vm.loadProfile(workerUid) }
                    vm.loadReviews(workerUid)
                }
                is WorkerViewModel.UiState.Error -> { b.progressBar.hide(); toast(state.msg) }
                else -> b.progressBar.hide()
            }
        }

        b.btnBack.setOnClickListener { finish() }

        b.btnHire.setOnClickListener {
            b.hireForm.visibility = if (b.hireForm.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
        b.btnWriteReview.setOnClickListener {
            b.reviewForm.visibility = if (b.reviewForm.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        b.btnSendRequest.setOnClickListener {
            val name  = b.etYourName.text.toString().trim()
            val phone = b.etYourPhone.text.toString().trim()
            if (name.isEmpty() || phone.isEmpty()) { toast(getString(R.string.error_required)); return@setOnClickListener }
            vm.sendHireRequest(HireRequest(
                workerUid    = workerUid,
                customerUid  = currentUid,
                customerName = name,
                customerPhone= phone,
                serviceName  = b.etServiceNeeded.text.toString().trim(),
                message      = b.etMessage.text.toString().trim()
            ))
        }

        b.btnSubmitReview.setOnClickListener {
            val rating = b.ratingBarInput.rating
            if (rating == 0f) { toast("Please give a rating"); return@setOnClickListener }
            vm.submitReview(Review(
                workerUid    = workerUid,
                customerUid  = currentUid,
                customerName = authVm.user.value?.name ?: "Customer",
                rating       = rating,
                comment      = b.etComment.text.toString().trim()
            ))
        }

        vm.loadProfile(workerUid); vm.loadServices(workerUid); vm.loadReviews(workerUid)
        authVm.checkSession()
    }
}