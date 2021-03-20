package pl.pollub.bsi.user.domain

import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.primary.AuthenticateError
import pl.pollub.bsi.user.boundary.primary.AuthenticateIdentityCommand
import pl.pollub.bsi.user.boundary.primary.AuthenticateIdentityCommandResponse
import pl.pollub.bsi.user.boundary.primary.CredentialChecker
import javax.inject.Singleton

@Singleton
class IdentityHandler (private val credentialChecker: CredentialChecker) {
    fun handle(command: AuthenticateIdentityCommand) : Either<AuthenticateError, AuthenticateIdentityCommandResponse> =
            credentialChecker.check(command)

}
