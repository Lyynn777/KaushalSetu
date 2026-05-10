package com.kaushal.setu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaushal.setu.data.model.User
import com.kaushal.setu.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repo = AuthRepository()

    private val _state = MutableLiveData<AuthState>()
    val state: LiveData<AuthState> = _state

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun checkSession() {
        val fu = repo.currentUser
        if (fu == null) { _state.value = AuthState.LoggedOut; return }
        viewModelScope.launch {
            repo.getUserData(fu.uid).fold(
                onSuccess = { _user.value = it; _state.value = AuthState.Success(it) },
                onFailure = { _state.value = AuthState.LoggedOut }
            )
        }
    }

    fun login(email: String, password: String) {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            repo.login(email, password).fold(
                onSuccess = { _user.value = it; _state.value = AuthState.Success(it) },
                onFailure = { _state.value = AuthState.Error(it.message ?: "Login failed") }
            )
        }
    }

    fun register(email: String, password: String, name: String, phone: String, userType: String) {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            repo.register(email, password, name, phone, userType).fold(
                onSuccess = { _user.value = it; _state.value = AuthState.Success(it) },
                onFailure = { _state.value = AuthState.Error(it.message ?: "Registration failed") }
            )
        }
    }

    fun logout() { repo.logout(); _user.value = null; _state.value = AuthState.LoggedOut }

    sealed class AuthState {
        object Loading   : AuthState()
        object LoggedOut : AuthState()
        data class Success(val user: User)   : AuthState()
        data class Error(val msg: String)    : AuthState()
    }
}