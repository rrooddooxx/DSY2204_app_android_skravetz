package com.nolineal.appskravetz.domain

data class RegisterUserDto(
    val firstName: String,
    val lastNameFather: String,
    val lastNameMother: String,
    val email: String,
    val password: String,
)