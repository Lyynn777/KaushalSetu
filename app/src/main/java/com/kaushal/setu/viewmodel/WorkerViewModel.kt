package com.kaushal.setu.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.setu.data.model.*
import com.kaushal.setu.data.repository.GeminiRepository
import com.kaushal.setu.data.repository.WorkerRepository
import kotlinx.coroutines.launch

class WorkerViewModel : ViewModel() {
    private val repo   = WorkerRepository()
    private val gemini = GeminiRepository()

    private val _profile     = MutableLiveData<WorkerProfile?>()
    val profile: LiveData<WorkerProfile?> = _profile

    private val _workers     = MutableLiveData<List<WorkerProfile>>()
    val workers: LiveData<List<WorkerProfile>> = _workers

    private val _services    = MutableLiveData<List<ServiceCard>>()
    val services: LiveData<List<ServiceCard>> = _services

    private val _reviews     = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> = _reviews

    private val _requests    = MutableLiveData<List<HireRequest>>()
    val requests: LiveData<List<HireRequest>> = _requests

    private val _aiText      = MutableLiveData<String>()
    val aiText: LiveData<String> = _aiText

    private val _ui          = MutableLiveData<UiState>()
    val ui: LiveData<UiState> = _ui

    // ── Profile ───────────────────────────────────────────────────────────────
    fun loadProfile(uid: String) = viewModelScope.launch {
        repo.getProfile(uid).fold(
            onSuccess = { _profile.value = it },
            onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
        )
    }

    fun saveProfile(p: WorkerProfile) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.saveProfile(p).fold(
                onSuccess = { _profile.value = p; _ui.value = UiState.Success("Profile saved") },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
            )
        }
    }

    fun uploadProfileImage(uid: String, uri: Uri) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.uploadProfileImage(uid, uri).fold(
                onSuccess = { _ui.value = UiState.ImageUploaded(it) },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Upload failed") }
            )
        }
    }

    fun uploadPortfolioImage(uid: String, uri: Uri) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.uploadPortfolioImage(uid, uri).fold(
                onSuccess = { _ui.value = UiState.Success("Photo added"); loadProfile(uid) },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Upload failed") }
            )
        }
    }

    // ── Services ──────────────────────────────────────────────────────────────
    fun loadServices(uid: String) = viewModelScope.launch {
        repo.getServices(uid).fold(
            onSuccess = { _services.value = it },
            onFailure = { }
        )
    }

    fun addService(s: ServiceCard) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.addService(s).fold(
                onSuccess = { _ui.value = UiState.Success("Service added"); loadServices(s.workerUid) },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
            )
        }
    }

    fun deleteService(uid: String, sid: String) = viewModelScope.launch {
        repo.deleteService(uid, sid).fold(
            onSuccess = { _ui.value = UiState.Success("Deleted"); loadServices(uid) },
            onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
        )
    }
    fun updateService(service: ServiceCard) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.updateService(service).fold(
                onSuccess = {
                    _ui.value = UiState.Success("Service updated")
                    loadServices(service.workerUid)
                },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Failed to update") }
            )
        }
    }
    // ── Workers (customer view) ────────────────────────────────────────────────
    fun loadAllWorkers() = viewModelScope.launch {
        repo.getAllWorkers().fold(
            onSuccess = { _workers.value = it },
            onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
        )
    }

    fun searchWorkers(category: String = "", location: String = "") = viewModelScope.launch {
        android.util.Log.d("KaushalSetu", "Searching — category: '$category' location: '$location'")
        repo.searchWorkers(category, location).fold(
            onSuccess = {
                android.util.Log.d("KaushalSetu", "Search result count: ${it.size}")
                _workers.value = it
            },
            onFailure = {
                android.util.Log.d("KaushalSetu", "Search failed: ${it.message}")
                _ui.value = UiState.Error(it.message ?: "Search failed")
            }
        )
    }

    // ── Reviews ───────────────────────────────────────────────────────────────
    fun loadReviews(uid: String) = viewModelScope.launch {
        repo.getReviews(uid).fold(onSuccess = { _reviews.value = it }, onFailure = {})
    }

    fun submitReview(r: Review) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.addReview(r).fold(
                onSuccess = { _ui.value = UiState.Success("Review submitted"); loadReviews(r.workerUid); loadProfile(r.workerUid) },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
            )
        }
    }

    // ── Hire Requests ─────────────────────────────────────────────────────────
    fun loadRequests(uid: String) = viewModelScope.launch {
        repo.getHireRequests(uid).fold(onSuccess = { _requests.value = it }, onFailure = {})
    }

    fun sendHireRequest(r: HireRequest) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            repo.sendHireRequest(r).fold(
                onSuccess = { _ui.value = UiState.Success("Request sent!") },
                onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
            )
        }
    }

    fun updateRequestStatus(id: String, status: String) = viewModelScope.launch {
        repo.updateRequestStatus(id, status).fold(
            onSuccess = { _ui.value = UiState.Success("Updated") },
            onFailure = { _ui.value = UiState.Error(it.message ?: "Failed") }
        )
    }

    // ── AI ────────────────────────────────────────────────────────────────────
    fun generateDescription(serviceType: String, years: Int) {
        _ui.value = UiState.Loading
        viewModelScope.launch {
            gemini.generateDescription(serviceType, years).fold(
                onSuccess = { _aiText.value = it; _ui.value = UiState.Success("Generated") },
                onFailure = { _ui.value = UiState.Error(it.message ?: "AI failed") }
            )
        }
    }

    // ── State ─────────────────────────────────────────────────────────────────
    sealed class UiState {
        object Loading : UiState()
        data class Success(val msg: String) : UiState()
        data class Error(val msg: String)   : UiState()
        data class ImageUploaded(val url: String) : UiState()
    }
}