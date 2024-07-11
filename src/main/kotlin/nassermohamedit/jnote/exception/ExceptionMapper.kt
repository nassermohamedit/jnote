package nassermohamedit.jnote.exception

import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestResponse
import org.jboss.resteasy.reactive.server.ServerExceptionMapper

class ExceptionMapper {

    @ServerExceptionMapper
    fun mapNotFoundException(e: NotFoundException): RestResponse<Void> {
        return RestResponse.status(Response.Status.NOT_FOUND)
    }

    @ServerExceptionMapper
    fun mapForbiddenException(e: ForbiddenException): RestResponse<Void> {
        return RestResponse.status(Response.Status.FORBIDDEN)
    }

    @ServerExceptionMapper
    fun mapIllegalArgumentException(e: IllegalArgumentException): RestResponse<Void> {
        return RestResponse.status(Response.Status.BAD_REQUEST)
    }

    @ServerExceptionMapper
    fun mapUniqueConstraintViolation(e: DatabaseError): RestResponse<Void> {
        return RestResponse.status(Response.Status.CONFLICT)
    }

    @ServerExceptionMapper
    fun mapServerError(e: ServerError): RestResponse<Void> {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR)
    }

    fun mapRuntimeException(e: RuntimeException): RestResponse<Void> {
        return RestResponse.status(Response.Status.INTERNAL_SERVER_ERROR)
    }
}