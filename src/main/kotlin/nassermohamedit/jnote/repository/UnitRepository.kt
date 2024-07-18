package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import nassermohamedit.jnote.entity.Unit

@ApplicationScoped
class UnitRepository: PanacheRepository<Unit> {
    fun findDefault(id: Long): Unit {
        return find("module.id = ?1 and self = ?2", id, true).singleResult();
    }

    fun findByModuleId(moduleId: Long): List<Unit> {
        return find("module.id = ?1 ORDER BY lastUpdated DESC", moduleId).list()
    }
}