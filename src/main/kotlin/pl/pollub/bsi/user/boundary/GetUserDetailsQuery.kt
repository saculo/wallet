package pl.pollub.bsi.user.boundary

import java.util.*

data class GetUserDetailsQuery (
        val passwordHidden: Boolean,
        val username: String,
        val id: UUID
) {
    fun withId(id: UUID) = GetUserDetailsQuery(this.passwordHidden, this.username, id)
}

