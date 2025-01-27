package com.nolineal.appskravetz.domain

data class User(
    val firstName: String,
    val lastNameFather: String,
    val lastNameMother: String,
    val email: String,
    var password: String,
)