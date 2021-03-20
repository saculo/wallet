package pl.pollub.bsi.wallet.boundary

import io.vavr.collection.List
import java.util.*

data class CreateWalletCommand(
        val passwords: List<PasswordCommand>,
        val userId: UUID,
        val userPassword: String
) {
    fun withEncryptedPasswords(passwords: List<PasswordCommand>) = CreateWalletCommand(passwords, this.userId, this.userPassword)
}
