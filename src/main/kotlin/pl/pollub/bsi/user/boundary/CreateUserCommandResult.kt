package pl.pollub.bsi.user.boundary

import java.util.*

data class CreateUserCommandResult(val message: String, val userId: UUID)
