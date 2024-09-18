package dev.fedezet.todo_list.controller

import dev.fedezet.todo_list.controller.dto.AuthLoginRequest
import dev.fedezet.todo_list.controller.dto.AuthRegisterRequest
import dev.fedezet.todo_list.service.IUserService
import dev.fedezet.todo_list.service.UserDetailsServiceImpl
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(@Autowired private val userDetailsService: UserDetailsServiceImpl,
                     @Autowired private val userService: IUserService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody authRegister: AuthRegisterRequest): ResponseEntity<Any> {
        if(userService.existsByEmail(authRegister.email))
            return ResponseEntity(mapOf("message" to "This email already exists"), HttpStatus.BAD_REQUEST)

        return ResponseEntity(this.userDetailsService.registerUser(authRegister), HttpStatus.CREATED)

    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody authLogin: AuthLoginRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity(this.userDetailsService.loginUser(authLogin), HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            ResponseEntity(mapOf("message" to "User or password invalid"), HttpStatus.BAD_REQUEST)
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, String?>> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to it.defaultMessage }
        return ResponseEntity(errors, HttpStatus.BAD_REQUEST)
    }
}