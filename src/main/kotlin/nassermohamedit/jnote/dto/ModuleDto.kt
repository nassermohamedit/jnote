package nassermohamedit.jnote.dto

import java.time.LocalDateTime

data class ModuleDto(
    val id: Long,
    val name: String,
    val description: String,
    val ownerId: Long,
    val lastUpdated: LocalDateTime,
    val creationTime: LocalDateTime
)
