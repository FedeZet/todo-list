package dev.fedezet.todo_list.service

import dev.fedezet.todo_list.persistence.entity.UserEntity
import dev.fedezet.todo_list.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(@Autowired private val userRepository: UserRepository): IUserService {
    override fun findUserByEmail(email: String): UserEntity {
        return userRepository.findUserByEmail(email).orElse(null)
    }

    override fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }
}