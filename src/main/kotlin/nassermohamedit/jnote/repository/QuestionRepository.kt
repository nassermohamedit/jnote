package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import nassermohamedit.jnote.entity.Question

@ApplicationScoped
class QuestionRepository: PanacheRepository<Question> {
    fun findByUnitId(unitId: Long): List<Question> {
        return find("unit.id = ?1", unitId).list()
    }
}