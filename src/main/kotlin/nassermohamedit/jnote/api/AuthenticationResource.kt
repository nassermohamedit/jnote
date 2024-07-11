package nassermohamedit.jnote.api;


import jakarta.annotation.security.PermitAll
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status
import nassermohamedit.jnote.dto.Login
import nassermohamedit.jnote.service.AuthService
import org.jboss.resteasy.reactive.RestResponse
import nassermohamedit.jnote.dto.Token;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AuthenticationResource @Inject constructor(
    private val authService: AuthService
) {

    @Path("/token")
    @POST
    @PermitAll
    fun login(login: Login): RestResponse<Token> {
        val jwt = authService.getJwtToken(login) ?: return RestResponse.status(Status.UNAUTHORIZED)
        return RestResponse.ok(jwt)
    }
}
