package nassermohamedit.jnote.dto

import java.time.LocalDateTime

data class UnitDto(
    val id: Long?,
    val name: String?,
    val description: String?,
    val moduleId: Long?,
    val lastUpdated: LocalDateTime?,
    val creationTime: LocalDateTime?,
    val self: Boolean?
)