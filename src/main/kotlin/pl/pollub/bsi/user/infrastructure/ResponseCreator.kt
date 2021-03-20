package pl.pollub.bsi.user.infrastructure

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.UserError
import javax.inject.Singleton

@Singleton
class ResponseCreator {
    fun created(response: Either<UserError, out Any>, status: HttpStatus): HttpResponse<Any> {
        return response
                .map { HttpResponse.ok(it).status(status) }
                .getOrElseGet { HttpResponse.badRequest(it) }
    }

    fun ok(response: Either<UserError, out Any>, status: HttpStatus): HttpResponse<Any> {
        return response
                .map { HttpResponse.created(it).status(status) }
                .getOrElseGet { HttpResponse.badRequest(it) }
    }
}
