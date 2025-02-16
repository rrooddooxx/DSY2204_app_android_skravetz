package com.nolineal.appskravetz.domain

enum class LoginFormStates(val value: String) {
    INITIAL("INITIAL"),
    EMPTY("EMPTY"),
    VALID("VALID"),
    INVALID("INVALID"),
    ERROR("ERROR"),
    SUCCESS("SUCCESS")
}