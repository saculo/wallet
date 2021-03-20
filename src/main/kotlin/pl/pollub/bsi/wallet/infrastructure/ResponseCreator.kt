package pl.pollub.bsi.wallet.infrastructure

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.vavr.control.Either
import pl.pollub.bsi.wallet.boundary.WalletError
import javax.inject.Singleton

@Singleton
class ResponseCreator {
    fun created(response: Either<WalletError, out Any>, status: HttpStatus): HttpResponse<Any> {
        return response
                .map { HttpResponse.ok(it).status(status) }
                .getOrElseGet { HttpResponse.badRequest(it) }
    }

    fun ok(response: Either<WalletError, out Any>, status: HttpStatus): HttpResponse<Any> {
        return response
                .map { HttpResponse.created(it).status(status) }
                .getOrElseGet { HttpResponse.badRequest(it) }
    }
}
