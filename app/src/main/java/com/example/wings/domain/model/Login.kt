package com.example.wings.domain.model

data class Login(
    val email: String,
    val password: String,
    var isLogin: Boolean = false,
    var message: String
)
