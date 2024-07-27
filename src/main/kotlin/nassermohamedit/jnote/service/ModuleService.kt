package nassermohamedit.jnote.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.ModuleDto
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.dto.UnitDto
import nassermohamedit.jnote.entity.*
import nassermohamedit.jnote.entity.Unit
import nassermohamedit.jnote.exception.ForbiddenException
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.repository.ModuleRepository
import nassermohamedit.jnote.repository.NoteRepository
import nassermohamedit.jnote.repository.UnitRepository
import java.time.LocalDateTime


@ApplicationScoped
class ModuleService @Inject constructor(
    private val moduleRepository: ModuleRepository,
    private val noteRepository: NoteRepository,
    private val unitRepository: UnitRepository
) {

    @Transactional
    fun createModule(module: Module, authId: Long): ModuleDto {
        val owner = User()
        owner.id = authId
        module.owner = owner
        val now = LocalDateTime.now()
        module.creationTime = now;
        module.lastUpdated = now;
        val defaultUnit = Unit()
        defaultUnit.name = module.name
        defaultUnit.self = true
        defaultUnit.module = module
        defaultUnit.description = module.description
        defaultUnit.creationTime = now
        defaultUnit.lastUpdated = now
        try {
            moduleRepository.persistAndFlush(module)
            unitRepository.persistAndFlush(defaultUnit)
            return ModuleDto(
                module.id,
                module.name,
                module.description,
                module.owner!!.id,
                module.lastUpdated,
                module.creationTime
            );
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    @Transactional
    fun deleteModuleById(id: Long, authId: Long) {
        val ownerId: Long = moduleRepository.findModuleOwnerId(id) ?: throw NotFoundException()
        checkModuleOwnership(ownerId, authId)
        moduleRepository.deleteById(id)
    }

    fun findAllModules(authId: Long): List<ModuleDto> {
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

    fun findModuleById(id: Long, authId: Long): ModuleDto {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!, authId)
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
    fun addNote(id: Long, noteData: NoteDto, authId: Long): NoteDto {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!, authId)
        val defaultUnit = unitRepository.findDefault(id)
        val note = Note()
        note.content = noteData.content
        if (noteData.questionId != null) {
            note.question = Question()
            note.question!!.id = noteData.questionId
        }
        note.creationTime = LocalDateTime.now()
        note.unit = defaultUnit
        try {
            noteRepository.persistAndFlush(note)
            return NoteDto(
                note.id,
                note.content,
                note.unit!!.id,
                note.question!!.id,
                note.creationTime
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    fun findAllNotes(id: Long, authId: Long): List<NoteDto> {
        val ownerId = moduleRepository.findModuleOwnerId(id) ?: throw NotFoundException()
        checkModuleOwnership(ownerId, authId)
        val defaultUnit = unitRepository.findDefault(id)
        val notes = noteRepository.findAllByUnitId(defaultUnit.id!!)
        return notes.map {
            note ->  NoteDto(
            note.id!!,
            note.content!!,
            note.unit!!.id!!,
            note.question!!.id,
            note.creationTime!!)
        }
    }

    private fun checkModuleOwnership(id: Long, authId: Long) {
        if (id != authId)
            throw ForbiddenException()
    }

    @Transactional
    fun updateModule(id: Long, update: Module, authId: Long): ModuleDto {
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

    @Transactional
    fun addUnit(id: Long, unit: UnitDto, authId: Long): UnitDto? {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!, authId)
        val newUnit = Unit()
        newUnit.name = unit.name
        newUnit.description = unit.description
        newUnit.module = module
        newUnit.self = false
        val now = LocalDateTime.now()
        newUnit.creationTime = now
        newUnit.lastUpdated = now
        try {
            unitRepository.persistAndFlush(newUnit)
            return UnitDto(
                newUnit.id,
                newUnit.name,
                newUnit.description,
                newUnit.module!!.id,
                newUnit.creationTime,
                newUnit.lastUpdated,
                newUnit.self
            )
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
    }

    fun findAllUnits(id: Long, authId: Long): List<UnitDto> {
        val module = moduleRepository.findById(id) ?: throw NotFoundException()
        checkModuleOwnership(module.owner!!.id!!, authId)
        val units = unitRepository.findByModuleId(id)
        return units.map {
            unit -> UnitDto(
            unit.id,
            unit.name,
            unit.description,
            unit.module!!.id,
            unit.creationTime,
            unit.lastUpdated,
            unit.self)
        }
    }
}
