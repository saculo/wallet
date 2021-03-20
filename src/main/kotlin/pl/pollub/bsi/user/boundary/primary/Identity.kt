package pl.pollub.bsi.user.boundary.primary

import pl.pollub.bsi.user.boundary.Algorithm
import java.util.*

class Identity(
        val id: Long,
        val userId: UUID,
        val username: String,
        val password: String,
        val chosenAlgorithm: Algorithm,
        val salt: UUID
) {
    fun withSalt(salt: UUID) : Identity = Identity(this.id, this.userId, this.username, this.password, this.chosenAlgorithm, salt)
    fun withPassword(password: String) : Identity = Identity(this.id, this.userId, this.username, password, this.chosenAlgorithm, this.salt)
    fun withAlgorithm(algorithm: Algorithm) : Identity = Identity(this.id, this.userId, this.username, this.password, algorithm, this.salt)

}
