package nassermohamedit.jnote.service

import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.entity.Note
import nassermohamedit.jnote.exception.ForbiddenException
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.repository.NoteRepository
import org.eclipse.microprofile.jwt.JsonWebToken


@RequestScoped
class NoteService @Inject constructor(private val noteRepository: NoteRepository, jwt: JsonWebToken) {

    private val authId = jwt.getClaim<String>("id").toLong()

    fun findNoteById(id: Long): NoteDto {
        val note = noteRepository.findById(id) ?: throw NotFoundException();

        if (note.module!!.owner!!.id!! != authId) {
            throw ForbiddenException()
        }
        return NoteDto(note.id!!, note.content!!, note.module!!.id!!, note.creationTime!!)
    }

    @Transactional
    fun deleteById(id: Long) {
        val note = noteRepository.findById(id) ?: throw NotFoundException()
        if (note.module!!.owner!!.id != authId) {
            throw ForbiddenException()
        }
        noteRepository.deleteById(id)
    }

    fun updateNote(id: Long, update: Note): NoteDto {
        val note = noteRepository.findById(id) ?: throw NotFoundException()
        if (note.module!!.owner!!.id!! != authId) {
            throw ForbiddenException()
        }
        note.content = update.content
        try {
            noteRepository.entityManager.merge(note)
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
        return NoteDto(note.id!!, note.content!!, note.module!!.id!!, note.creationTime!!)
    }
}