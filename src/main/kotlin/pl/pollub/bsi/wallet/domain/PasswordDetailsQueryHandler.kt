package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.wallet.boundary.*
import pl.pollub.bsi.wallet.infrastructure.PasswordEncrypter
import java.util.*

@Context
class PasswordDetailsQueryHandler(
        private val userHandler: UserHandler,
        private val walletRepository: WalletRepository
) {
    fun showOne(userId: UUID, username: String, passwordId: Long, passwordHidden: Boolean): Either<WalletError, PasswordDetailsQueryResponse> =
            userHandler.getIdentity(userId)
                    .filterOrElse({ it.username == username }, { WalletError("User havent permissions to show passwords for this user.") })

                    .flatMap { user ->
                        walletRepository.getPasswordById(passwordId).toEither(WalletError("Password not found."))
                                .filterOrElse({ it.userId == userId }, { WalletError("User don't have permission to this password.") })
                                .map { buildPassword(it, passwordHidden, user.password) }
                    }


    private fun buildPassword(password: Password, passwordHidden: Boolean, masterPassword: String): PasswordDetailsQueryResponse {
        if (passwordHidden) {
            return PasswordDetailsQueryResponse(password.username, password.password, password.url, password.description)
        }
        return PasswordDetailsQueryResponse(
                password.username,
                PasswordEncrypter.AES.decrypt(password.password, masterPassword),
                password.url,
                password.description
        )
    }
}
