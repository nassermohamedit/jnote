package nassermohamedit.jnote.dto

import java.time.LocalDateTime

data class NoteDto(
    val id: Long?,
    val content: String?,
    val unitId: Long?,
    val creationTime: LocalDateTime?
)
