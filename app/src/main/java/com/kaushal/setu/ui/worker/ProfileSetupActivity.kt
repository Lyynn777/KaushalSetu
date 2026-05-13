package com.kaushal.setu.ui.worker

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.R
import com.kaushal.setu.data.model.WorkerProfile
import com.kaushal.setu.databinding.ActivityProfileSetupBinding
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.WorkerViewModel
import com.kaushal.setu.ui.common.BaseActivity
class ProfileSetupActivity : BaseActivity() {
    private lateinit var b: ActivityProfileSetupBinding
    private val vm: WorkerViewModel by viewModels()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var imageUrl = ""
    private var pendingUri: Uri? = null

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { pendingUri = it; Glide.with(this).load(it).circleCrop().into(b.ivAvatar) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityProfileSetupBinding.inflate(layoutInflater)
        setContentView(b.root)
        setupSpinner()
        setupObservers()
        b.ivAvatar.setOnClickListener { pickImage.launch("image/*") }
        b.btnBack.setOnClickListener { finish() }
        b.btnSave.setOnClickListener { if (validate()) { if (pendingUri != null) vm.uploadProfileImage(uid, pendingUri!!) else save() } }
        vm.loadProfile(uid)
    }

    private fun setupSpinner() {
        val cats = resources.getStringArray(R.array.skill_categories)
        b.spinnerCategory.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cats)
    }

    private fun setupObservers() {
        vm.profile.observe(this) { p ->
            p ?: return@observe
            b.etName.setText(p.name); b.etPhone.setText(p.phone)
            b.etLocation.setText(p.location); b.etExperience.setText(p.yearsOfExperience.toString())
            b.etBio.setText(p.bio); b.switchAvailable.isChecked = p.available
            imageUrl = p.profileImageUrl
            if (p.profileImageUrl.isNotEmpty()) Glide.with(this).load(p.profileImageUrl).circleCrop().into(b.ivAvatar)
            val cats = resources.getStringArray(R.array.skill_categories)
            val pos = cats.indexOf(p.skillCategory); if (pos >= 0) b.spinnerCategory.setSelection(pos)
        }
        vm.ui.observe(this) { state ->
            when (state) {
                is WorkerViewModel.UiState.Loading      -> b.progressBar.visibility = View.VISIBLE
                is WorkerViewModel.UiState.ImageUploaded -> { imageUrl = state.url; save() }
                is WorkerViewModel.UiState.Success      -> { b.progressBar.visibility = View.GONE; if (state.msg == "Profile saved") { toast(state.msg); finish() } }
                is WorkerViewModel.UiState.Error        -> { b.progressBar.visibility = View.GONE; toast(state.msg) }
                else -> b.progressBar.visibility = View.GONE
            }
        }
    }

    private fun validate(): Boolean {
        val req = getString(R.string.error_required); var ok = true
        if (b.etName.text.isNullOrBlank())       { b.tilName.error = req; ok = false } else b.tilName.error = null
        if (b.etPhone.text.isNullOrBlank())      { b.tilPhone.error = req; ok = false } else b.tilPhone.error = null
        if (b.etLocation.text.isNullOrBlank())   { b.tilLocation.error = req; ok = false } else b.tilLocation.error = null
        if (b.etExperience.text.isNullOrBlank()) { b.tilExperience.error = req; ok = false } else b.tilExperience.error = null
        return ok
    }

    private fun save() {
        vm.saveProfile(WorkerProfile(
            uid = uid,
            name = b.etName.text.toString().trim(),
            phone = b.etPhone.text.toString().trim(),
            skillCategory = b.spinnerCategory.selectedItem.toString(),
            yearsOfExperience = b.etExperience.text.toString().toIntOrNull() ?: 0,
            location = b.etLocation.text.toString().trim(),
            bio = b.etBio.text.toString().trim(),
            profileImageUrl = imageUrl,
            available = b.switchAvailable.isChecked
        ))
    }
}