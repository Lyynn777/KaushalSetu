package com.kaushal.setu.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceCard(
    val id: String = "",
    val workerUid: String = "",
    val serviceName: String = "",
    val price: String = "",
    val description: String = "",
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable