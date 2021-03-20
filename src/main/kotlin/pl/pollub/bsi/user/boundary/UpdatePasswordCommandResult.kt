package pl.pollub.bsi.user.boundary

import java.util.*

data class UpdatePasswordCommandResult(val message: String, val userId: UUID)
