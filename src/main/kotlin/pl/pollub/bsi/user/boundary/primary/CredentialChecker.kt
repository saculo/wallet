package pl.pollub.bsi.user.boundary.primary

import io.vavr.control.Either

interface CredentialChecker {
    fun check(command: AuthenticateIdentityCommand): Either<AuthenticateError, AuthenticateIdentityCommandResponse>
}
