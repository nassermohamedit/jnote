package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import nassermohamedit.jnote.entity.Note


@ApplicationScoped
class NoteRepository: PanacheRepository<Note> {
    fun findAllByUnitId(unitId: Long): List<Note> {
        return find("unit.id = ?1 ORDER BY creationTime DESC", unitId).list()
    }

}