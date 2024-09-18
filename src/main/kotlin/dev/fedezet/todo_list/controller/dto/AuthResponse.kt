package dev.fedezet.todo_list.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthResponse(
    @JsonProperty
    val token: String,
)