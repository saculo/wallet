package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import pl.pollub.bsi.wallet.boundary.DeletePasswordCommandResponse
import pl.pollub.bsi.wallet.boundary.UserHandler
import pl.pollub.bsi.wallet.boundary.WalletError
import pl.pollub.bsi.wallet.boundary.WalletRepository
import java.util.*

@Context
class DeletePasswordCommandHandler(
        private val userHandler: UserHandler,
        private val walletRepository: WalletRepository,
        private val jdbi: Jdbi
) {
    fun delete(userId: UUID, username: String, passwordId: Long): Either<WalletError, DeletePasswordCommandResponse> =
            jdbi.inTransactionUnchecked {
                userHandler.getIdentity(userId)
                        .filterOrElse({ it.username == username }, { WalletError("User havent permissions to delete password for this user.") })
                        .flatMap {
                            walletRepository.getPasswordById(passwordId).toEither(WalletError("Password not found."))
                                    .filterOrElse({ it.userId == userId }, { WalletError("User don't have permission to this password.") })
                        }
                        .map { walletRepository.delete(it) }
            }
}
