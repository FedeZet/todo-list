package dev.fedezet.todo_list.persistence.entity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*

@Entity
@Table(name = "task")
@JsonIgnoreProperties("user")
data class TaskEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val title: String,
    val description: String,
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity
)

data class PaginatedResponse(
    val data: List<TaskEntity>,
    val page: Int,
    val limit: Int,
    val total: Int
)