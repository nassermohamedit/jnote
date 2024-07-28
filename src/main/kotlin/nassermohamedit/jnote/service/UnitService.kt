package nassermohamedit.jnote.service

import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.dto.QuestionDto
import nassermohamedit.jnote.dto.UnitDto
import nassermohamedit.jnote.entity.Note
import nassermohamedit.jnote.entity.Question
import nassermohamedit.jnote.entity.Unit
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.repository.NoteRepository
import nassermohamedit.jnote.repository.QuestionRepository
import nassermohamedit.jnote.repository.UnitRepository
import java.time.LocalDateTime

@ApplicationScoped
class UnitService @Inject constructor(
    private val unitRepository: UnitRepository,
    private val noteRepository: NoteRepository,
    private val questionRepository: QuestionRepository
){
    
    fun findUnitById(id: Long, authId: Long): UnitDto? {
        val unit = unitRepository.findById(id) ?: throw NotFoundException()
        checkOwnership(unit, authId)
        return unitToDto(unit)
    }

    fun updateUnit(id: Long, updates: UnitDto, authId: Long): UnitDto {
        val unit = unitRepository.findById(id) ?: throw NotFoundException()
        checkOwnership(unit, authId)
        unit.name = updates.name
        unit.description = updates.description
        unit.lastUpdated = LocalDateTime.now()
        try {
            unitRepository.entityManager.merge(unit)
            return unitToDto(unit)
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }


    @Transactional
    fun deleteById(id: Long, authId: Long) {
        val unit = unitRepository.findById(id)
        checkOwnership(unit, authId)
        unitRepository.deleteById(id)
    }

    @Transactional
    fun saveNote(id: Long, newNote: NoteDto, authId: Long): NoteDto {
        val unit = unitRepository.findById(id)
        checkOwnership(unit, authId)
        val note = Note()
        if (newNote.questionId != null) {
            note.question = Question()
            note.question!!.id = newNote.questionId
        }
        note.unit = unit
        note.content = newNote.content
        val now = LocalDateTime.now()
        note.creationTime = now
        unit.lastUpdated = now
        unit.module!!.lastUpdated = now

        try {
            noteRepository.persistAndFlush(note)
            return NoteDto(
                note.id,
                note.content,
                unit.id,
                note.question!!.id,
                note.creationTime
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    fun findAllNotes(id: Long, authId: Long): List<NoteDto>? {
        val unit = unitRepository.findById(id)
        checkOwnership(unit, authId)
        val notes  = noteRepository.findAllByUnitId(id)
        return notes.map {
            note -> NoteDto(
                note.id,
                note.content,
                unit.id,
                note.question!!.id,
                note.creationTime
            )
        }
    }

    private fun checkOwnership(unit: Unit, authId: Long) {
        if (unit.module!!.owner!!.id != authId) {
            throw UnauthorizedException()
        }
    }

    private fun unitToDto(unit: Unit): UnitDto {
        return UnitDto(
            unit.id,
            unit.name,
            unit.description,
            unit.module?.id,
            unit.lastUpdated,
            unit.creationTime,
            unit.self
        )
    }

    @Transactional
    fun addQuestion(id: Long, questionData: QuestionDto, authId: Long): QuestionDto? {
        val unit = unitRepository.findById(id)
        checkOwnership(unit, authId)
        val question = Question()
        question.question = questionData.question
        question.unit = unit
        val now = LocalDateTime.now()
        unit.lastUpdated = now
        unit.module!!.lastUpdated = now
        try {
            questionRepository.persistAndFlush(question)
            return QuestionDto(
                question.id,
                question.question!!,
                unit.id,
                question.isAnswered,
                question.answer
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    fun findAllQuestions(id: Long, authId: Long): List<QuestionDto> {
        val unit = unitRepository.findById(id)
        checkOwnership(unit, authId)
        val questions = questionRepository.findByUnitId(id);
        return questions.map {
            question -> QuestionDto(
                question.id,
                question.question!!,
                question.unit!!.id,
                question.isAnswered,
                question.answer
            )
        }
    }
}



