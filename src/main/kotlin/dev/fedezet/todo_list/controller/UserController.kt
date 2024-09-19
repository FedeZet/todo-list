package dev.fedezet.todo_list.controller

import dev.fedezet.todo_list.persistence.entity.UserEntity
import dev.fedezet.todo_list.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(@Autowired private val userService: UserService) {

    @GetMapping
    fun getUser(): ResponseEntity<UserEntity> {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name

        return ResponseEntity.ok(userService.findUserByEmail(email))
    }
}