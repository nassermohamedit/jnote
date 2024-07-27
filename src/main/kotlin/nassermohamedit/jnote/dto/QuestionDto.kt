package nassermohamedit.jnote.dto

data class QuestionDto(
    val id: Long?,
    val question: String,
    val unitId: Long?,
    val answered: Boolean?,
    val answer: String?
)
