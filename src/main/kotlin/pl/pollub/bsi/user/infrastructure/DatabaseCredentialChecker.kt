package pl.pollub.bsi.user.infrastructure

import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.IdentityRepository
import pl.pollub.bsi.user.boundary.primary.*
import javax.inject.Singleton

@Singleton
class DatabaseCredentialChecker(
        private val identityRepository: IdentityRepository
) : CredentialChecker {
    override fun check(command: AuthenticateIdentityCommand): Either<AuthenticateError, AuthenticateIdentityCommandResponse> =
            identityRepository.getByUsername(GetIdentityByUsernameQuery(command.username))
                    .filter {
                        command.username == it.username && PasswordEncrypter.encrypt(
                                it.chosenAlgorithm.type,
                                command.password,
                                it.salt.toString()
                        ) == it.password
                    }
                    .map { identity -> AuthenticateIdentityCommandResponse(identity.username) }
                    .toEither(AuthenticateError("Wrong credentials!"))

}
