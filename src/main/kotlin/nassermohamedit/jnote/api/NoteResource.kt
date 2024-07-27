package nassermohamedit.jnote.api

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.entity.Note
import nassermohamedit.jnote.service.NoteService
import org.jboss.resteasy.reactive.RestResponse

@Path(API.NOTES_SCOPE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class NoteResource @Inject constructor(
    private val noteService: NoteService
) {
    

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    fun getNote(@PathParam("id") id: Long): RestResponse<NoteDto> {
        val note = noteService.findNoteById(id);
        return RestResponse.ok(note)
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    fun updateNote(@PathParam("id") id: Long, note: NoteDto): RestResponse<NoteDto> {
        val updated = noteService.updateNote(id, note)
        return RestResponse.ok(updated)
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    fun deleteNote(@PathParam("id") id: Long): RestResponse<Void> {
        noteService.deleteById(id)
        return RestResponse.noContent()
    }
}