package nassermohamedit.jnote.dto

import java.time.LocalDateTime

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val creationTime: LocalDateTime
)
