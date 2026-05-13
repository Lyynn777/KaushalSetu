package com.kaushal.setu.ui.worker

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.kaushal.setu.R
import com.kaushal.setu.data.model.ServiceCard
import com.kaushal.setu.databinding.ActivityAddServiceBinding
import com.kaushal.setu.ui.common.BaseActivity
import com.kaushal.setu.utils.toast
import com.kaushal.setu.viewmodel.WorkerViewModel

class AddServiceActivity : BaseActivity() {
    private lateinit var b: ActivityAddServiceBinding
    private val vm: WorkerViewModel by viewModels()
    private val uid get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private var editingService: ServiceCard? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(b.root)

        // Check if we received a service to edit
        @Suppress("DEPRECATION")
        editingService = intent.getParcelableExtra("SERVICE_TO_EDIT")

        // Pre-fill all fields if editing
        editingService?.let { service ->
            b.etServiceName.setText(service.serviceName)
            b.etPrice.setText(service.price)
            b.etDescription.setText(service.description)
            b.switchAvailable.isChecked = service.isAvailable
            b.btnAdd.text = getString(R.string.btn_update_service)
        }

        vm.ui.observe(this) { state ->
            when (state) {
                is WorkerViewModel.UiState.Loading -> setLoading(true)
                is WorkerViewModel.UiState.Success -> {
                    setLoading(false)
                    if (state.msg == "Service added" || state.msg == "Service updated") {
                        toast(state.msg)
                        finish()
                    } else toast(state.msg)
                }
                is WorkerViewModel.UiState.Error -> {
                    setLoading(false)
                    toast(state.msg)
                }
                else -> setLoading(false)
            }
        }

        vm.aiText.observe(this) { b.etDescription.setText(it) }

        b.btnBack.setOnClickListener { finish() }

        b.btnGenerate.setOnClickListener {
            val name = b.etServiceName.text.toString().trim()
            if (name.isEmpty()) {
                b.tilServiceName.error = getString(R.string.error_required)
                return@setOnClickListener
            }
            b.tilServiceName.error = null
            val years = b.etExperience.text.toString().toIntOrNull() ?: 0
            vm.generateDescription(name, years)
        }

        b.btnAdd.setOnClickListener {
            val name  = b.etServiceName.text.toString().trim()
            val price = b.etPrice.text.toString().trim()
            if (name.isEmpty())  { b.tilServiceName.error = getString(R.string.error_required); return@setOnClickListener }
            if (price.isEmpty()) { b.tilPrice.error = getString(R.string.error_required); return@setOnClickListener }
            b.tilServiceName.error = null
            b.tilPrice.error = null

            val existing = editingService
            if (existing != null) {
                // Edit mode — keep same id and workerUid
                vm.updateService(
                    existing.copy(
                        serviceName = name,
                        price       = price,
                        description = b.etDescription.text.toString().trim(),
                        isAvailable = b.switchAvailable.isChecked
                    )
                )
            } else {
                // Add mode — create new service
                vm.addService(
                    ServiceCard(
                        workerUid   = uid,
                        serviceName = name,
                        price       = price,
                        description = b.etDescription.text.toString().trim(),
                        isAvailable = b.switchAvailable.isChecked
                    )
                )
            }
        }
    }

    private fun setLoading(on: Boolean) {
        b.progressBar.visibility = if (on) View.VISIBLE else View.GONE
        b.btnAdd.isEnabled       = !on
        b.btnGenerate.isEnabled  = !on
    }
}