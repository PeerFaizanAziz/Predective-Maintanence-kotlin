package com.example.predictivemaintenence.modelClass

data class LoginRequestmodel(
    val deviceToken: String,
    val password: String,
    val username: String
)