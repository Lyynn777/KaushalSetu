package com.kaushal.setu.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = "",
    val userType: String = "customer",   // "worker" | "customer"
    val createdAt: Long = System.currentTimeMillis()
)