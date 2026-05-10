package com.kaushal.setu.data.model

data class Review(
    val id: String = "",
    val workerUid: String = "",
    val customerUid: String = "",
    val customerName: String = "",
    val rating: Float = 0f,
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)