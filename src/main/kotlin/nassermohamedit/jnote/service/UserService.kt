package nassermohamedit.jnote.service

import jakarta.enterprise.context.RequestScoped
import jakarta.inject.Inject
import jakarta.persistence.PersistenceException
import jakarta.transaction.Transactional
import nassermohamedit.jnote.dto.UserDto
import nassermohamedit.jnote.entity.Role
import nassermohamedit.jnote.entity.User
import nassermohamedit.jnote.exception.ForbiddenException
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.exception.DatabaseError
import nassermohamedit.jnote.repository.UserRepository
import org.eclipse.microprofile.jwt.JsonWebToken
import java.time.LocalDateTime


@RequestScoped
class UserService @Inject constructor(private val userRepository: UserRepository, jwt: JsonWebToken) {

    private val id = jwt.getClaim<String>("id").toLong()

    fun findById(id: Long): nassermohamedit.jnote.dto.UserDto {
        val user = userRepository.findById(id) ?: throw NotFoundException()
        return nassermohamedit.jnote.dto.UserDto(user.id!!, user.name!!, user.username!!, user.email!!, user.creationTime!!)
    }

    @Transactional
    fun createUser(user: User): UserDto {
        val role = Role()
        role.id = 2; // TODO !!!!!!!
        user.role = role;
        val now = LocalDateTime.now();
        user.creationTime = now
        try {
            userRepository.persistAndFlush(user);
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
        return nassermohamedit.jnote.dto.UserDto(user.id!!, user.name!!, user.username!!, user.email!!, now)
    }

    @Transactional
    fun delete(id: Long) {
        checkIdentity(id)
        userRepository.deleteById(id);
    }

    @Transactional
    fun updateUser(id: Long, user: User): UserDto {
        checkIdentity(id)
        user.id = id
        val role = Role()
        role.id = 2; // TODO !!!!!!!
        user.role = role
        try {
            userRepository.entityManager.merge(user);
        } catch (e: PersistenceException) {
            throw DatabaseError()
        }
        return UserDto(user.id!!, user.name!!, user.username!!, user.email!!, user.creationTime!!)

    }

    private fun checkIdentity(id: Long) {
        if (id != this.id) {
            throw ForbiddenException()
        }
    }
}