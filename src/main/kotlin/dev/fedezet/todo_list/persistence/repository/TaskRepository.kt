package dev.fedezet.todo_list.persistence.repository

import dev.fedezet.todo_list.persistence.entity.TaskEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository: CrudRepository<TaskEntity, Long> {

    fun findByUserId(userId: Long, pageable: Pageable): Page<TaskEntity>
    fun countByUserId(userId: Long): Int
}