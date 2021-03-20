package pl.pollub.bsi.user.infrastructure

import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.UserDetails
import java.util.*

class InternalAuthenticationResponse(val userDetails: UserDetails) : AuthenticationResponse {
    private val AUTHENTICATED_MESSAGE: String = "Authenticated!"

    override fun getUserDetails(): Optional<UserDetails> = Optional.of(userDetails)

    override fun getMessage(): Optional<String> = Optional.of(AUTHENTICATED_MESSAGE)
}
