package dev.fedezet.todo_list.service

import dev.fedezet.todo_list.persistence.entity.TaskEntity

interface ITaskService {
    fun getTasks(userId: Long, page: Int, limit: Int): MutableIterable<TaskEntity>
    fun findTaskById(id: Long): TaskEntity
    fun isExistTask(id: Long): Boolean
    fun saveTask(taskEntity: TaskEntity): TaskEntity
    fun deleteTask(id: Long)
    fun isAuthorizedTask(taskEntity: TaskEntity, userId: Long): Boolean
    fun getTasksTotal(userId: Long): Int
}