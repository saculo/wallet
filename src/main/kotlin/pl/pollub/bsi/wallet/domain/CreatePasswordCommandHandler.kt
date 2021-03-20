package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.collection.List
import io.vavr.control.Either
import io.vavr.control.Option
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import pl.pollub.bsi.wallet.boundary.*
import pl.pollub.bsi.wallet.infrastructure.PasswordEncrypter
import pl.pollub.bsi.wallet.infrastructure.SynchronousUserHandler
import java.util.*

@Context
class CreatePasswordCommandHandler(
        private val userHandler: SynchronousUserHandler,
        private val walletRepository: WalletRepository,
        private val jdbi: Jdbi
) {
    fun create(userId: UUID, username: String, command: CreatePasswordCommand): Either<WalletError, CreatePasswordCommandResponse> =
            jdbi.inTransactionUnchecked {
                userHandler.getIdentity(userId)
                        .filterOrElse({ it.username == username }, { WalletError("User havent permissions to create password for this user.") })
                        .flatMap { save(command, it.password, userId) }
                        .map { CreatePasswordCommandResponse("Password created.") }
            }

    private fun save(command: CreatePasswordCommand, masterPassword: String, userId: UUID): Either<WalletError, CreateWalletCommandResponse> {
        return Option.of(command)
                .toEither(WalletError("Unexpected error."))
                .map {
                    walletRepository.save(CreateWalletCommand(List.of(
                            PasswordCommand(
                                    it.username,
                                    PasswordEncrypter.AES.encrypt(it.password, masterPassword),
                                    it.url,
                                    it.description,
                                    userId
                            )),
                            userId,
                            masterPassword
                    ))
                }
    }
}
