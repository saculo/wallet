package pl.pollub.bsi.user.infrastructure

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.*
import pl.pollub.bsi.wallet.boundary.PasswordCommand
import pl.pollub.bsi.wallet.boundary.UpdatePasswordsCommand
import pl.pollub.bsi.wallet.domain.WalletFacade

@Context
class SynchronousWalletHandler(private val walletFacade: WalletFacade) : WalletHandler {
    override fun create(command: CreateWalletCommand): Either<UserError, CreateWalletCommandResponse> =
            walletFacade.create(pl.pollub.bsi.wallet.boundary.CreateWalletCommand(
                    command.passwords.map {
                        PasswordCommand(it.username, it.password, it.url, it.description, command.userId)
                    },
                    command.userId,
                    command.userPassword
            ))
                    .mapLeft { UserError(it.message) }
                    .map { CreateWalletCommandResponse(it.message) }

    override fun update(command: UpdateWalletCommand): Either<UserError, UpdateWalletCommandResponse> =
            walletFacade.updatePasswords(UpdatePasswordsCommand(
                    command.userId,
                    command.currentPassword,
                    command.oldPassword
            ))
                    .mapLeft { UserError(it.message) }
                    .map { UpdateWalletCommandResponse(it.message) }
}
