package nassermohamedit.jnote.dto

import nassermohamedit.jnote.entity.Question
import java.time.LocalDateTime

data class NoteDto(
    val id: Long?,
    val content: String?,
    val unitId: Long?,
    val questionId: Long?,
    val creationTime: LocalDateTime?
)
