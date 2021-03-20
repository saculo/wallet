package pl.pollub.bsi.wallet.infrastructure

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.user.domain.UserFacade
import pl.pollub.bsi.wallet.boundary.UserData
import pl.pollub.bsi.wallet.boundary.UserHandler
import pl.pollub.bsi.wallet.boundary.WalletError
import java.util.*

@Context
class SynchronousUserHandler(private val userFacade: UserFacade) : UserHandler {
    override fun getIdentity(userId: UUID) : Either<WalletError, UserData> {
        return userFacade.userById(userId)
                .mapLeft { WalletError(it.message) }
                .map { UserData(it.userId, it.username, it.password) }
    }
}
