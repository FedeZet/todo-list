package dev.fedezet.todo_list.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AuthLoginRequest(@field:NotBlank(message = "Email cannot be empty")
                            @field:Email(message = "You must enter an email")
                            val email: String,
                            @field:NotBlank(message = "Password cannot be empty")
                            @field:Size(message = "Password must be longer than 4 characters", min = 4)
                            val password: String)

