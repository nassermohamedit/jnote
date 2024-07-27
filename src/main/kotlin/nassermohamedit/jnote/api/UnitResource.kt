package nassermohamedit.jnote.api

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.dto.QuestionDto
import nassermohamedit.jnote.dto.UnitDto
import nassermohamedit.jnote.service.UnitService
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.RestResponse

@Path("/api/units")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UnitResource @Inject constructor(private val unitService: UnitService) {

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    fun getUnit(
        @PathParam("id") id: Long,
        @Context jwt: JsonWebToken
    ): RestResponse<UnitDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val unitDto = unitService.findUnitById(id, authId)
        return RestResponse.ok(unitDto)
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    fun updateUnit(
        @PathParam("id") id: Long, updates: UnitDto,
        @Context jwt: JsonWebToken
    ): RestResponse<UnitDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val unitDto = unitService.updateUnit(id, updates, authId)
        return RestResponse.ok(unitDto)
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    fun deleteUnit(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<Void> {
        val authId = jwt.getClaim<String>("id").toLong()

        try {
            unitService.deleteById(id, authId)
        } catch (ignored: Exception) {
            // DELETE is Idempotent
        }
        return RestResponse.noContent()
    }

    @POST
    @Path("/{id}")
    @RolesAllowed("user")
    fun addNote(@PathParam("id") id: Long, note: NoteDto, @Context jwt: JsonWebToken): RestResponse<NoteDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val saved = unitService.saveNote(id, note, authId)
        return RestResponse.ok(saved)
    }

    @GET
    @Path("/{id}/notes")
    @RolesAllowed("user")
    fun getNotes(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<List<NoteDto>> {
        val authId = jwt.getClaim<String>("id").toLong()
        val notes = unitService.findAllNotes(id, authId)
        return RestResponse.ok(notes)
    }

    @POST
    @Path("/{id}/questions")
    @RolesAllowed("user")
    fun addQuestion(@PathParam("id") id: Long, question: QuestionDto, @Context jwt: JsonWebToken): RestResponse<QuestionDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val created = unitService.addQuestion(id, question, authId)
        return RestResponse.ok(created)
    }


    @GET
    @Path("/{id}/questions")
    @RolesAllowed("user")
    fun getAZlQuestions(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<List<QuestionDto>> {
        val authId = jwt.getClaim<String>("id").toLong()
        val questions = unitService.findAllQuestions(id, authId)
        return RestResponse.ok(questions)
    }
}