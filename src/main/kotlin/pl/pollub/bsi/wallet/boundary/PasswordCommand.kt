package pl.pollub.bsi.wallet.boundary

import java.util.*

data class PasswordCommand(val username: String, val password: String, val url: String, val description: String, val userId: UUID) {
    fun withEncryptedPassword(password: String) = PasswordCommand(this.username, password, this.url, this.description, this.userId)

}
