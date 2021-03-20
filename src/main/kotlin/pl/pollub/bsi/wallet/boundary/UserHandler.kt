package pl.pollub.bsi.wallet.boundary

import io.vavr.control.Either
import java.util.*

interface UserHandler {
    fun getIdentity(userId: UUID) : Either<WalletError, UserData>
}
