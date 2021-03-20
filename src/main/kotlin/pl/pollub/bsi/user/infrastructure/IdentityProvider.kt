package pl.pollub.bsi.user.infrastructure

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import pl.pollub.bsi.user.boundary.primary.AuthenticateIdentityCommand
import pl.pollub.bsi.user.domain.IdentityHandler
import java.util.*
import javax.inject.Singleton

@Singleton
class IdentityProvider(
        private val identityHandler: IdentityHandler
) : AuthenticationProvider {

    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
        val identity = authenticationRequest?.identity.toString()
        val password = authenticationRequest?.secret.toString()

        return Flowable.create( { flow ->
            val authenticationResult = identityHandler.handle(AuthenticateIdentityCommand(identity, password))
            if (authenticationResult.isRight) {
                flow.onNext(InternalAuthenticationResponse(
                        UserDetails(identity, Collections.emptyList()))
                )
                flow.onComplete()
            }
            else {
                flow.onError(AuthenticationException(AuthenticationFailed(authenticationResult.left.message())))
            }

        }, BackpressureStrategy.MISSING)
    }
}
