package pl.pollub.bsi.wallet.boundary

import java.util.*

data class UpdatePasswordsCommand(val userId: UUID, val currentPassword: String, val oldPassword: String)
