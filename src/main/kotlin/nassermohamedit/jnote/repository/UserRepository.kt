package nassermohamedit.jnote.repository

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import nassermohamedit.jnote.entity.User
import nassermohamedit.jnote.projections.Credentials

@ApplicationScoped
class UserRepository: PanacheRepository<User> {

    fun findCredentialsByUsername(username: String): Credentials? {
        return find("username", username).
                project(Credentials::class.java).
                firstResult();
    }
}