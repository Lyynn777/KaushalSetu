package com.kaushal.setu.data.model

data class WorkerProfile(
    val uid: String = "",
    val name: String = "",
    val phone: String = "",
    val skillCategory: String = "",
    val yearsOfExperience: Int = 0,
    val location: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val portfolioImages: List<String> = emptyList(),
    val averageRating: Float = 0f,
    val totalRatings: Int = 0,
    val available: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)