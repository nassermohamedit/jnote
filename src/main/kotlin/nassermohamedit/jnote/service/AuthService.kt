package nassermohamedit.jnote.service

import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.jwt.build.Jwt
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import nassermohamedit.jnote.dto.Login
import nassermohamedit.jnote.repository.UserRepository
import nassermohamedit.jnote.dto.Token

@ApplicationScoped
class AuthService @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getJwtToken(login: Login): Token? {
        val credentials = userRepository.findCredentialsByUsername(login.username) ?: return null
        if (credentials.password != login.password) {
            return null
        }
        val token = Jwt.issuer("localhost").
                upn(login.username).
                groups(credentials.role.name).
                claim("id", "" + credentials.id).
                expiresIn(3600*24*7).
                sign()
        return Token(token);
    }
}
