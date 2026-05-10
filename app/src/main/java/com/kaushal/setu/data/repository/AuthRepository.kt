package com.kaushal.setu.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.kaushal.setu.data.model.User
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db   = FirebaseFirestore.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun register(
        email: String, password: String,
        name: String, phone: String, userType: String
    ): Result<User> = try {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("UID missing")
        val user = User(uid = uid, email = email, name = name, phone = phone, userType = userType)
        db.collection("users").document(uid).set(user).await()
        Result.success(user)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun login(email: String, password: String): Result<User> = try {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: throw Exception("UID missing")
        val user = db.collection("users").document(uid).get().await()
            .toObject(User::class.java) ?: throw Exception("User data missing")
        Result.success(user)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getUserData(uid: String): Result<User> = try {
        val user = db.collection("users").document(uid).get().await()
            .toObject(User::class.java) ?: throw Exception("User not found")
        Result.success(user)
    } catch (e: Exception) { Result.failure(e) }

    fun logout() = auth.signOut()
}