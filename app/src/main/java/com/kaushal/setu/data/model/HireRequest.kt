package com.kaushal.setu.data.model

data class HireRequest(
    val id: String = "",
    val workerUid: String = "",
    val workerName: String = "",
    val customerUid: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val serviceName: String = "",
    val message: String = "",
    val status: String = "pending",    // "pending" | "accepted" | "rejected"
    val createdAt: Long = System.currentTimeMillis()
)