package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import nassermohamedit.jnote.entity.Note


@ApplicationScoped
class NoteRepository: PanacheRepository<Note> {
    fun findAllByModuleId(moduleId: Long): List<Note> {
        return find("module.id = ?1 ORDER BY creationTime DESC", moduleId).list()
    }

}