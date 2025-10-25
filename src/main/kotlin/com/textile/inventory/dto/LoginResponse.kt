package com.textile.inventory.dto

data class LoginResponse(
    val token: String,
    val type: String = "Bearer",
    val username: String,
    val role: String
)
