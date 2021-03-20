package pl.pollub.bsi.user.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import io.vavr.control.Option
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import pl.pollub.bsi.user.boundary.CreateWalletCommand
import pl.pollub.bsi.user.boundary.IdentityRepository
import pl.pollub.bsi.user.boundary.UserError
import pl.pollub.bsi.user.boundary.WalletHandler
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.CreateUserCommandSuccess
import pl.pollub.bsi.user.infrastructure.PasswordEncrypter
import java.util.*

@Context
class CreateUserCommandHandler(private val identityRepository: IdentityRepository,
                               private val walletHandler: WalletHandler,
                               private val jdbi: Jdbi
                               ) {
    fun handle(command: CreateUserCommand): Either<UserError, CreateUserCommandSuccess> {
        val salt = UUID.randomUUID()
        return Option.of(command)
                .filter { !identityRepository.existsByUsername(it.username) }
                .toEither { UserError("User with given username already exists.") }
                .map {
                    it.withHashedPassword(PasswordEncrypter.encrypt(
                            it.algorithm.type,
                            it.password,
                            salt.toString()
                    ))
                }
                .flatMap { create(it, salt) }
    }

    private fun create(command: CreateUserCommand, salt: UUID): Either<UserError, CreateUserCommandSuccess> {
        return jdbi.inTransaction<Either<UserError, CreateUserCommandSuccess>, Exception> {
            Option.of(command)
                    .toEither(UserError("Unexpected error"))
                    .map { identityRepository.save(command, salt) }
                    .toEither(UserError("User creation failed."))
                    .flatMap { result ->
                        walletHandler.create(
                                CreateWalletCommand(
                                        command.passwords.passwords,
                                        result.userId,
                                        command.password
                                )
                        )
                    }
                    .map { CreateUserCommandSuccess(it.message) }
        }
    }
}
