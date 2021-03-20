package pl.pollub.bsi.user.boundary

import io.vavr.control.Either

interface WalletHandler {
    fun create(command : CreateWalletCommand) : Either<UserError, CreateWalletCommandResponse>
    fun update(command : UpdateWalletCommand) : Either<UserError, UpdateWalletCommandResponse>
}
