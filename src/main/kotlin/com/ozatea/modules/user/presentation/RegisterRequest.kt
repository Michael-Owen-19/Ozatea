package com.ozatea.modules.user.presentation

data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String,
    val name: String
)