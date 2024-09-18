package dev.fedezet.todo_list.persistence.repository

import dev.fedezet.todo_list.persistence.entity.UserEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository: CrudRepository<UserEntity, Long> {

    fun findUserByEmail(email: String): Optional<UserEntity>
    fun existsByEmail(email: String): Boolean
}