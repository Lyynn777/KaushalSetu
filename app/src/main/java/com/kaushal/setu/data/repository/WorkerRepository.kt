package com.kaushal.setu.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.kaushal.setu.data.model.HireRequest
import com.kaushal.setu.data.model.Review
import com.kaushal.setu.data.model.ServiceCard
import com.kaushal.setu.data.model.WorkerProfile
import kotlinx.coroutines.tasks.await
import java.util.UUID

class WorkerRepository {
    private val db      = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // ── Profile ───────────────────────────────────────────────────────────────

    suspend fun saveProfile(profile: WorkerProfile): Result<Unit> = try {
        db.collection("workers").document(profile.uid).set(profile).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getProfile(uid: String): Result<WorkerProfile> = try {
        val doc = db.collection("workers").document(uid).get().await()
        val profile = doc.toObject(WorkerProfile::class.java)
            ?: throw Exception("Profile not found")
        Result.success(profile)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getAllWorkers(): Result<List<WorkerProfile>> = try {
        val snap = db.collection("workers").get().await()
        Result.success(snap.documents.mapNotNull { it.toObject(WorkerProfile::class.java) })
    } catch (e: Exception) { Result.failure(e) }

    suspend fun searchWorkers(category: String, location: String): Result<List<WorkerProfile>> = try {
        var query = db.collection("workers").whereEqualTo("isAvailable", true)
        if (category.isNotBlank()) query = query.whereEqualTo("skillCategory", category)
        val snap = query.get().await()
        var list = snap.documents.mapNotNull { it.toObject(WorkerProfile::class.java) }
        if (location.isNotBlank())
            list = list.filter { it.location.contains(location, ignoreCase = true) }
        Result.success(list)
    } catch (e: Exception) { Result.failure(e) }

    // ── Images ────────────────────────────────────────────────────────────────

    suspend fun uploadProfileImage(uid: String, uri: Uri): Result<String> = try {
        val ref = storage.reference.child("profiles/$uid.jpg")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        db.collection("workers").document(uid).update("profileImageUrl", url).await()
        Result.success(url)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun uploadPortfolioImage(uid: String, uri: Uri): Result<String> = try {
        val ref = storage.reference.child("portfolio/$uid/${UUID.randomUUID()}.jpg")
        ref.putFile(uri).await()
        val url = ref.downloadUrl.await().toString()
        val current = db.collection("workers").document(uid).get().await()
            .toObject(WorkerProfile::class.java)
        val updated = (current?.portfolioImages ?: emptyList()).toMutableList().apply { add(url) }
        db.collection("workers").document(uid).update("portfolioImages", updated).await()
        Result.success(url)
    } catch (e: Exception) { Result.failure(e) }

    // ── Services ──────────────────────────────────────────────────────────────

    suspend fun addService(service: ServiceCard): Result<Unit> = try {
        val id = db.collection("workers").document(service.workerUid)
            .collection("services").document().id
        db.collection("workers").document(service.workerUid)
            .collection("services").document(id).set(service.copy(id = id)).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getServices(workerUid: String): Result<List<ServiceCard>> = try {
        val snap = db.collection("workers").document(workerUid)
            .collection("services").get().await()
        Result.success(snap.documents.mapNotNull { it.toObject(ServiceCard::class.java) })
    } catch (e: Exception) { Result.failure(e) }

    suspend fun deleteService(workerUid: String, serviceId: String): Result<Unit> = try {
        db.collection("workers").document(workerUid)
            .collection("services").document(serviceId).delete().await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun updateService(service: ServiceCard): Result<Unit> = try {
        db.collection("workers").document(service.workerUid)
            .collection("services").document(service.id).set(service).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }
    // ── Reviews ───────────────────────────────────────────────────────────────

    suspend fun addReview(review: Review): Result<Unit> = try {
        val id = db.collection("workers").document(review.workerUid)
            .collection("reviews").document().id
        db.collection("workers").document(review.workerUid)
            .collection("reviews").document(id).set(review.copy(id = id)).await()
        val all = db.collection("workers").document(review.workerUid)
            .collection("reviews").get().await()
            .documents.mapNotNull { it.toObject(Review::class.java) }
        val avg = all.map { it.rating }.average().toFloat()
        db.collection("workers").document(review.workerUid)
            .update("averageRating", avg, "totalRatings", all.size).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getReviews(workerUid: String): Result<List<Review>> = try {
        val snap = db.collection("workers").document(workerUid)
            .collection("reviews").get().await()
        Result.success(snap.documents.mapNotNull { it.toObject(Review::class.java) })
    } catch (e: Exception) { Result.failure(e) }

    // ── Hire Requests ─────────────────────────────────────────────────────────

    suspend fun sendHireRequest(request: HireRequest): Result<Unit> = try {
        val id = db.collection("hire_requests").document().id
        db.collection("hire_requests").document(id).set(request.copy(id = id)).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }

    suspend fun getHireRequests(workerUid: String): Result<List<HireRequest>> = try {
        val snap = db.collection("hire_requests")
            .whereEqualTo("workerUid", workerUid).get().await()
        Result.success(snap.documents.mapNotNull { it.toObject(HireRequest::class.java) })
    } catch (e: Exception) { Result.failure(e) }

    suspend fun updateRequestStatus(requestId: String, status: String): Result<Unit> = try {
        db.collection("hire_requests").document(requestId).update("status", status).await()
        Result.success(Unit)
    } catch (e: Exception) { Result.failure(e) }
}