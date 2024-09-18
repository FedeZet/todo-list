package dev.fedezet.todo_list.service

import dev.fedezet.todo_list.persistence.entity.TaskEntity
import dev.fedezet.todo_list.persistence.repository.TaskRepository
import dev.fedezet.todo_list.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class TaskService(@Autowired private val taskRepository: TaskRepository,
                  @Autowired private val userRepository: UserRepository): ITaskService {

    override fun getTasks(userId: Long, page: Int, limit: Int): MutableIterable<TaskEntity> {
        val pageable = PageRequest.of(page - 1, limit)
        return taskRepository.findByUserId(userId, pageable).content
    }

    override fun findTaskById(id: Long): TaskEntity {
        return taskRepository.findById(id).orElse(null)
    }

    override fun isExistTask(id: Long): Boolean {
        return taskRepository.existsById(id)
    }

    override fun saveTask(taskEntity: TaskEntity): TaskEntity {
        return taskRepository.save(taskEntity)
    }

    override fun deleteTask(id: Long) {
        return taskRepository.deleteById(id)
    }

    override fun isAuthorizedTask(taskEntity: TaskEntity, userId: Long): Boolean {
        return taskEntity.user.id == userId
    }

    override fun getTasksTotal(userId: Long): Int {
        return taskRepository.countByUserId(userId)
    }
}