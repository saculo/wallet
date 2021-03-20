package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import io.vavr.control.Try
import pl.pollub.bsi.wallet.boundary.CreateWalletCommand
import pl.pollub.bsi.wallet.boundary.CreateWalletCommandResponse
import pl.pollub.bsi.wallet.boundary.WalletError
import pl.pollub.bsi.wallet.boundary.WalletRepository
import pl.pollub.bsi.wallet.infrastructure.PasswordEncrypter

@Context
class CreateWalletHandler(private val walletRepository: WalletRepository) {
    fun create(command: CreateWalletCommand): Either<WalletError, CreateWalletCommandResponse> =
            Try.of { encryptPasswords(command) }
                    .map { walletRepository.save(it) }
                    .toEither(WalletError("Wallet creation failure."))

    private fun encryptPasswords(command: CreateWalletCommand) : CreateWalletCommand {
        val hashedPasswords = command.passwords
                .map { it.withEncryptedPassword(PasswordEncrypter.AES.encrypt(it.password, command.userPassword)) }
        return command.withEncryptedPasswords(hashedPasswords)
    }
}
