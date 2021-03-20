package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.wallet.boundary.*

@Context
class WalletFacade(private val getWalletHandler: GetWalletQueryHandler,
                   private val createWalletCommandHandler: CreateWalletHandler,
                   private val updatePasswordsCommandHandler: UpdatePasswordHandler) {
    fun getWallet(query: GetWalletUserQuery): GetWalletUserQueryResponse = getWalletHandler.getWallet(query)
    fun create(command: CreateWalletCommand): Either<WalletError, CreateWalletCommandResponse> =
            createWalletCommandHandler.create(command)
    fun updatePasswords(command: UpdatePasswordsCommand) : Either<WalletError, UpdatePasswordsCommandResponse> =
            updatePasswordsCommandHandler.update(command)
}
