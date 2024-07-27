package nassermohamedit.jnote.service

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.QuestionDto
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.repository.QuestionRepository
import java.time.LocalDateTime

@ApplicationScoped
class QuestionService @Inject constructor(private val questionRepository: QuestionRepository){

    fun get(id: Long, authId: Long): QuestionDto {
        val question = questionRepository.findById(id) ?: throw NotFoundException()
        if (question.unit!!.module!!.owner!!.id !=authId)
            throw UnauthorizedException()
        return QuestionDto(
            question.id,
            question.question!!,
            question.unit!!.id,
            question.isAnswered,
            question.answer
        )
    }

    @Transactional
    fun update(id: Long, update: QuestionDto, authId: Long): QuestionDto {
        val question = questionRepository.findById(id)
        if (question.unit!!.module!!.owner!!.id !=authId)
            throw UnauthorizedException()
        question.question = update.question
        if (update.answered == true) {
            question.isAnswered = update.answered
        }
        question.answer = update.answer
        val now = LocalDateTime.now()
        question.unit!!.lastUpdated = now
        question.unit!!.module!!.lastUpdated = now
        try {
            questionRepository.persistAndFlush(question)
            return QuestionDto(
                question.id,
                question.question!!,
                question.unit!!.id,
                question.isAnswered,
                question.answer
            )
        } catch (e: Exception) {
            throw DatabaseError()
        }
    }

    @Transactional
    fun delete(id: Long, authId: Long) {
        val question = questionRepository.findById(id)
        if (question.unit!!.module!!.owner!!.id !=authId)
            throw UnauthorizedException()
        try {
            questionRepository.deleteById((id))
        } catch (E: Exception) {
            throw DatabaseError()
        }
    }
}