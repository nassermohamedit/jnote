package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.NoResultException
import nassermohamedit.jnote.entity.Module


@ApplicationScoped
class ModuleRepository: PanacheRepository<Module> {

    fun findByOwnerId(id: Long): List<Module> {
        return find("owner.id = ?1 ORDER BY lastUpdated DESC", id).list()
    }

    fun findModuleOwnerId(id: Long): Long? {
        val query = entityManager.createQuery(
            "SELECT m.owner.id FROM Module m WHERE m.id = :id", Long::class.java
        )
        query.setParameter("id", id)
        return try {
            query.singleResult
        } catch (e: NoResultException) {
            null
        }
    }
}