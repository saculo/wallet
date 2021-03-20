package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import io.vavr.control.Option
import pl.pollub.bsi.wallet.boundary.*
import pl.pollub.bsi.wallet.infrastructure.PasswordEncrypter

@Context
class UpdatePasswordHandler(private val walletRepository: WalletRepository) {
    fun update(command: UpdatePasswordsCommand): Either<WalletError, UpdatePasswordsCommandResponse> =
            Option.of(command)
                    .toEither(WalletError("Command not found!"))
                    .flatMap { updatePasswords(command) }

    fun updatePasswords(command: UpdatePasswordsCommand) : Either<WalletError, UpdatePasswordsCommandResponse> {
        val passwords = walletRepository.getWalletByUserId(GetWalletUserQuery(command.userId))
                .map { it.withEncryptedPassword(PasswordEncrypter.AES.decrypt(it.password, command.oldPassword)) }
                .map { it.withEncryptedPassword(PasswordEncrypter.AES.encrypt(it.password, command.currentPassword)) }
        if (passwords.isEmpty) {
            return Either.left(WalletError("Passwords not found!"))
        }
        return Either.right(walletRepository.update(passwords))
    }

}
