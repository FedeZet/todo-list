package dev.fedezet.todo_list.controller

import dev.fedezet.todo_list.persistence.entity.PaginatedResponse
import dev.fedezet.todo_list.persistence.entity.TaskEntity
import dev.fedezet.todo_list.persistence.entity.UserEntity
import dev.fedezet.todo_list.service.ITaskService
import dev.fedezet.todo_list.service.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/todos")
class TaskController(@Autowired private val taskService: ITaskService,
                     @Autowired private val userService: IUserService) {

    @PostMapping
    fun createTask(@RequestBody task: TaskEntity): ResponseEntity<TaskEntity> {
        task.user = this.getUser()

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(task))
    }

    @PutMapping("/{idReceived}")
    fun updateTask(@PathVariable idReceived: Long, @RequestBody taskReceived: TaskEntity): ResponseEntity<Any> {
        if(!taskService.isExistTask(idReceived)) return ResponseEntity.notFound().build()

        val task = taskService.findTaskById(idReceived)
        if(!taskService.isAuthorizedTask(task, getUser().id)) {
            return ResponseEntity(mapOf("message" to "Forbidden"), HttpStatus.FORBIDDEN)
        }

        val taskUpdated: TaskEntity = taskService.saveTask(taskReceived.copy(id = idReceived, user = task.user))

        return ResponseEntity.status(HttpStatus.CREATED).body(taskUpdated)
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Any> {
        if(!taskService.isExistTask(id)) return ResponseEntity.notFound().build()

        val task = taskService.findTaskById(id)
        if(!taskService.isAuthorizedTask(task, getUser().id)) {
            return ResponseEntity(mapOf("message" to "Unauthorized"), HttpStatus.UNAUTHORIZED)
        }

        taskService.deleteTask(id)

        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getTasks(@RequestParam page: Int = 1, @RequestParam limit: Int = 10): PaginatedResponse {
        val tasks = taskService.getTasks(getUser().id, page, limit)
        val total = taskService.getTasksTotal(getUser().id)

        val taskResponse = tasks.map { task ->
            TaskEntity(
                id = task.id,
                title = task.title,
                description = task.description,
                user = task.user
            )
        }

        return PaginatedResponse(
            data = taskResponse,
            page = page,
            limit = limit,
            total = total
        )
    }

    fun getUser(): UserEntity {
        val auth = SecurityContextHolder.getContext().authentication
        val email = auth.name

        return userService.findUserByEmail(email)
    }
}