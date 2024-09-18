package dev.fedezet.todo_list.service

import dev.fedezet.todo_list.persistence.entity.UserEntity

interface IUserService {
    fun findUserByEmail(email: String): UserEntity
    fun existsByEmail(email: String): Boolean
}