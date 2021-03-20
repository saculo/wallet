package pl.pollub.bsi.user.boundary.primary

import pl.pollub.bsi.user.boundary.Algorithm

data class CreateUserCommand (
        val username: String,
        val password: String,
        val algorithm: Algorithm,
        val passwords: Passwords
        ) {

    fun withHashedPassword(hashedPassword: String) = CreateUserCommand(
            this.username,
            hashedPassword,
            this.algorithm,
            this.passwords
    )
}
