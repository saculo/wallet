package pl.pollub.bsi.user.boundary

import java.util.*

data class UpdateWalletCommand(val userId: UUID, val currentPassword: String, val oldPassword: String)
