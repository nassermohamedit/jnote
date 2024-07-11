package nassermohamedit.jnote.service

import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.ModuleDto
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.entity.Module
import nassermohamedit.jnote.entity.Note
import nassermohamedit.jnote.entity.User
import nassermohamedit.jnote.exception.ForbiddenException
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.repository.ModuleRepository
import nassermohamedit.jnote.repository.NoteRepository
import org.eclipse.microprofile.jwt.JsonWebToken
import java.time.LocalDateTime


@RequestScoped
class ModuleService @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val noteRepository: NoteRepository,
    jwt: JsonWebToken
) {
    private val authId = jwt.getClaim<String>("id").toLong()

    @Transactional
    fun createModule(module: Module): Module {
        val owner = User()
        owner.id = authId
        module.owner = owner
        val now = LocalDateTime.now()
        module.creationTime = now;
        module.lastUpdated = now;
        try {
            moduleRepository.persistAndFlush(module)
            return module;
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    @Transactional
    fun deleteModuleById(id: Long) {
        val ownerId: Long = moduleRepository.findModuleOwnerId(id) ?: throw NotFoundException()
        checkModuleOwnership(ownerId)
        moduleRepository.deleteById(id)
    }

    fun findAllModules(): List<ModuleDto> {
        return moduleRepository.findByOwnerId(authId).map {
            module: Module ->  ModuleDto(
            module.id!!,
            module.name!!,
            module.description!!,
            module.owner!!.id!!,
            module.lastUpdated!!,
            module.creationTime!!)
        }
    }

    fun findModuleById(id: Long): ModuleDto {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!)
        return ModuleDto(
            module.id!!,
            module.name!!,
            module.description!!,
            module.owner!!.id!!,
            module.lastUpdated!!,
            module.creationTime!!
        )
    }

    @Transactional
    fun addNote(id: Long, note: Note): NoteDto {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!)
        val now = LocalDateTime.now()
        module.lastUpdated = now
        note.module = module
        note.creationTime = now
        moduleRepository.entityManager.merge(module)
        try {
            noteRepository.persistAndFlush(note)
            return NoteDto(
                note.id!!,
                note.content!!,
                note.module!!.id!!,
                note.creationTime!!
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    fun findAllNotes(id: Long): List<NoteDto> {
        val ownerId = moduleRepository.findModuleOwnerId(id) ?: throw NotFoundException()
        checkModuleOwnership(ownerId)
        val notes = noteRepository.findAllByModuleId(id)
        return notes.map {
            note: Note ->  NoteDto(note.id!!, note.content!!, note.module!!.id!!, note.creationTime!!)
        }
    }

    private fun checkModuleOwnership(id: Long) {
        if (id != authId)
            throw ForbiddenException()
    }

    @Transactional
    fun updateModule(id: Long, update: Module): ModuleDto {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        module.name = update.name
        module.description = update.description
        module.lastUpdated = LocalDateTime.now()
        try {
            moduleRepository.entityManager.merge(module)
            return ModuleDto(
                module.id!!,
                module.name!!,
                module.description!!,
                module.owner!!.id!!,
                module.lastUpdated!!,
                module.creationTime!!
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }
}
