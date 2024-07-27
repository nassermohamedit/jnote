package nassermohamedit.jnote.api

import jakarta.annotation.security.PermitAll
import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.UserDto
import nassermohamedit.jnote.entity.User
import nassermohamedit.jnote.service.UserService
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder
import java.net.URI

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserResource @Inject constructor(private val userService: UserService) {

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    fun getUserById(@PathParam("id") id: Long): RestResponse<UserDto> {
        val user = userService.findById(id);
        return ResponseBuilder.ok(user).build()
    }

    @POST
    @PermitAll
    fun createUser(user: User): RestResponse<UserDto> {
        val createdUser = userService.createUser(user);
        return ResponseBuilder
            .created<UserDto>(URI.create("${API.USERS_SCOPE}/${createdUser.id}"))
            .entity(createdUser)
            .build()
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    fun deleteUser(@PathParam("id") id: Long): RestResponse<UserDto> {
        userService.delete(id)
        return ResponseBuilder.noContent<UserDto>().build()
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    fun updateUser(@PathParam("id") id: Long, user: User): RestResponse<UserDto> {
        val updatedUser = userService.updateUser(id, user);
        return ResponseBuilder.ok<UserDto>().entity(updatedUser).build()
    }
}