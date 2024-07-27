package nassermohamedit.jnote.api

import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.QuestionDto
import nassermohamedit.jnote.service.QuestionService
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.RestResponse


@Path("/api/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class QuestionResource @Inject constructor(private val questionService: QuestionService) {

    @GET
    @Path("/{id}")
    fun getQuestion(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<QuestionDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val question = questionService.get(id, authId)
        return RestResponse.ok(question)
    }

    @PUT
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, update: QuestionDto, @Context jwt: JsonWebToken): RestResponse<QuestionDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val question = questionService.update(id, update, authId)
        return RestResponse.ok(question)
    }

    @DELETE
    @Path("/{id}")
    fun delete(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<Void> {
        val authId = jwt.getClaim<String>("id").toLong()
         questionService.delete(id, authId)
        return RestResponse.noContent()
    }

}