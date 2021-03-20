package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.collection.List
import io.vavr.control.Either
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import pl.pollub.bsi.wallet.boundary.*
import pl.pollub.bsi.wallet.infrastructure.PasswordEncrypter
import java.util.*

@Context
class UpdatePasswordCommandHandler(
        private val userHandler: UserHandler,
        private val walletRepository: WalletRepository,
        private val jdbi: Jdbi
) {
    fun update(userId: UUID, username: String, passwordId: Long, command: UpdatePasswordCommand): Either<WalletError, UpdatePasswordCommandResponse> =
            jdbi.inTransactionUnchecked {
                userHandler.getIdentity(userId)
                        .filterOrElse({ it.username == username }, { WalletError("User havent permissions to update password for this user.") })
                        .flatMap { user ->
                            walletRepository.getPasswordById(passwordId).toEither(WalletError("Password not found."))
                                    .filterOrElse({ it.userId == userId }, { WalletError("User don't have permission to this password.") })
                                    .map { it.withEncryptedPassword(PasswordEncrypter.AES.encrypt(command.password, user.password)) }
                        }
                        .map {
                            walletRepository.update(
                                    List.of(
                                            Password(
                                                    it.id,
                                                    it.username,
                                                    it.password,
                                                    it.url,
                                                    it.description,
                                                    it.userId
                                            )
                                    )
                            )
                        }
                        .map { UpdatePasswordCommandResponse(it.message) }
            }
}
