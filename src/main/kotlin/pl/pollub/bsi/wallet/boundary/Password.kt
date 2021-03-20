package pl.pollub.bsi.wallet.boundary

import java.util.*

data class Password (
        val id: Long,
        val username: String,
        val password: String,
        val url: String,
        val description: String,
        val userId: UUID
) {
    fun withEncryptedPassword(password: String) = Password(this.id, this.username, password, this.url, this.description, this.userId)
}
