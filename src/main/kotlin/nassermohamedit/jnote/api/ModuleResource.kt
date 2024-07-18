package nassermohamedit.jnote.api

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import nassermohamedit.jnote.dto.ModuleDto
import nassermohamedit.jnote.dto.NoteDto
import nassermohamedit.jnote.dto.UnitDto
import nassermohamedit.jnote.entity.Module
import nassermohamedit.jnote.exception.NotFoundException
import nassermohamedit.jnote.service.ModuleService
import org.eclipse.microprofile.jwt.JsonWebToken
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI


@Path("/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ModuleResource @Inject constructor(private val moduleService: ModuleService) {

    @GET
    @RolesAllowed("user")
    fun getAllModules(@Context jwt: JsonWebToken): RestResponse<List<ModuleDto>> {
        val authId = jwt.getClaim<String>("id").toLong()
        val modules = moduleService.findAllModules(authId);
        return RestResponse.ok(modules)
    }

    @GET
    @Path("/{id}")
    @RolesAllowed("user")
    fun getModuleInfo(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<ModuleDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val module = moduleService.findModuleById(id, authId)
        return RestResponse.ok(module)
    }

    @POST
    @RolesAllowed("user")
    fun createModule(module: Module, @Context jwt: JsonWebToken): RestResponse<Module> {
        val authId = jwt.getClaim<String>("id").toLong()
        val created = moduleService.createModule(module, authId)
        return RestResponse.created(URI.create("${API.MODULES_SCOPE}/${created.id}"))
    }

    @PUT
    @Path("/{id}")
    @RolesAllowed("user")
    fun updateModule(@PathParam("id") id: Long, update: Module,@Context jwt: JsonWebToken): RestResponse<ModuleDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val updated = moduleService.updateModule(id, update, authId)
        return RestResponse.ok(updated)
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("user")
    fun deleteModule(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<Void> {
        val authId = jwt.getClaim<String>("id").toLong()
        try {
            moduleService.deleteModuleById(id, authId)
        } catch (ignored: NotFoundException) {
            // DELETE is idempotent
        }
        return RestResponse.noContent()
    }

    @POST
    @Path("/{id}")
    @RolesAllowed("user")
    fun addNote(@PathParam("id") id: Long, note: NoteDto, @Context jwt: JsonWebToken): RestResponse<NoteDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val addedNote = moduleService.addNote(id, note, authId);
        return RestResponse.ok(addedNote);
    }

    @GET
    @Path("/{id}/notes")
    @RolesAllowed("user")
    fun getAllNotes(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<List<NoteDto>> {
        val authId = jwt.getClaim<String>("id").toLong()
        val notes = moduleService.findAllNotes(id, authId)
        return  RestResponse.ok(notes)
    }

    @POST
    @Path("/{id}/units")
    @RolesAllowed("user")
    fun addUnit(@PathParam("id") id: Long, unit: UnitDto, @Context jwt: JsonWebToken): RestResponse<UnitDto> {
        val authId = jwt.getClaim<String>("id").toLong()
        val addedUnit = moduleService.addUnit(id, unit, authId);
        return RestResponse.ok(addedUnit);
    }

    @GET
    @Path("/{id}/units")
    @RolesAllowed("user")
    fun getAllUnits(@PathParam("id") id: Long, @Context jwt: JsonWebToken): RestResponse<List<UnitDto>> {
        val authId = jwt.getClaim<String>("id").toLong()
        val units = moduleService.findAllUnits(id, authId);
        return RestResponse.ok(units);
    }

}
