package pl.pollub.bsi.user.boundary

import pl.pollub.bsi.user.boundary.primary.Passwords

data class User(
        val id: Long,
        val login: String,
        val password: String,
        val algorithm: Algorithm,
        val salt: String,
        val isPasswordHashed: Boolean,
        val passwords: Passwords
) {

}
