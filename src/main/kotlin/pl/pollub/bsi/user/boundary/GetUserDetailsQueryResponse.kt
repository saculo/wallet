package pl.pollub.bsi.user.boundary

import pl.pollub.bsi.user.boundary.primary.Passwords
import java.util.*

data class GetUserDetailsQueryResponse(
        val id: UUID,
        val login: String,
        val password: String,
        val algorithm: Algorithm,
        val salt: UUID,
        val passwords: Passwords
)
