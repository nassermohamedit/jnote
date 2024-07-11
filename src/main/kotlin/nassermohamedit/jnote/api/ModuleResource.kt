package nassermohamedit.jnote.api

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.ModuleDto
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.entity.Module
import nassermohamedit.jnote.entity.Note
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.service.ModuleService
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI


@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ModuleResource @Inject constructor(private val moduleService: ModuleService) {

    @GET
    @RolesAllowed("user")
    fun getAllModules(): RestResponse<List<ModuleDto>> {
        val modules = moduleService.findAllModules();
        return RestResponse.ok(modules)
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    fun getModuleInfo(@PathParam("id") id: Long): RestResponse<ModuleDto> {
        val module = moduleService.findModuleById(id)
        return RestResponse.ok(module)
    }

    @POST
    @RolesAllowed("user")
    fun createModule(module: Module): RestResponse<Module> {
        val created = moduleService.createModule(module)
        return RestResponse.created(URI.create("${API.MODULES_SCOPE}/${created.id}"))
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    fun updateModule(@PathParam("id") id: Long, update: Module): RestResponse<ModuleDto> {
        val updated = moduleService.updateModule(id, update)
        return RestResponse.ok(updated)
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    fun deleteModule(@PathParam("id") id: Long): RestResponse<Void> {
        try {
            moduleService.deleteModuleById(id)
        } catch (ignored: NotFoundException) {
            // DELETE is idempotent
        }
        return RestResponse.noContent()
    }

    @POST
    @Path("/{id}")
    @RolesAllowed("user")
    fun addNote(@PathParam("id") id: Long, note: Note): RestResponse<NoteDto> {
        val addedNote = moduleService.addNote(id, note);
        return RestResponse.created(URI.create("${API.NOTES_SCOPE}/${addedNote.id}"))
    }

    @GET
    @Path("/{id}/notes")
    @RolesAllowed("user")
    fun getAllNotes(@PathParam("id") id: Long): RestResponse<List<NoteDto>> {
        val notes = moduleService.findAllNotes(id)
        return  RestResponse.ok(notes)
    }
}
