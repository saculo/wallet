package pl.pollub.bsi.user.boundary

import io.vavr.collection.List
import pl.pollub.bsi.user.boundary.primary.Password
import java.util.*

data class CreateWalletCommand(
        val passwords: List<Password>,
        val userId: UUID,
        val userPassword: String
)
