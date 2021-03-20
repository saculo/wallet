package pl.pollub.bsi.user.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import io.vavr.control.Option
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import pl.pollub.bsi.user.boundary.*
import pl.pollub.bsi.user.boundary.primary.Identity
import pl.pollub.bsi.user.infrastructure.PasswordEncrypter
import java.util.*
import java.util.function.Predicate

@Context
class UpdatePasswordCommandHandler(
        private val identityRepository: IdentityRepository,
        private val walletHandler: WalletHandler,
        private val jdbi: Jdbi
) {
    fun update(userId: UUID, username: String, command: UpdatePasswordCommand): Either<UserError, UpdateWalletCommandResponse> {
        val user = identityRepository.getById(userId)
                .toEither(UserError("User with given id doesn't exists."))
                .filterOrElse({ it.username == username }, { UserError("User havent permissions to update this user.") })

        if (user.isLeft) {
            return Either.left(user.left)
        }

        return jdbi.inTransactionUnchecked {
            Option.of(command)
                    .toEither(UserError("Command not given!"))
                    .flatMap { updatePassword(userId, it) }
                    .flatMap { walletHandler.update(UpdateWalletCommand(userId, it.password, user.get().password)) }
        }
    }

    private fun updatePassword(userId: UUID, command: UpdatePasswordCommand): Either<UserError, Identity> {
        return identityRepository.getById(userId)
                .toEither(UserError("User not found."))
                .map { it.withSalt(UUID.randomUUID()) }
                .map { it.withAlgorithm(command.algorithm) }
                .map { it.withPassword(PasswordEncrypter.encrypt(command.algorithm.type, command.password, it.salt.toString())) }
                .map { identityRepository.update(it) }
    }

}
