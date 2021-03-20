package pl.pollub.bsi.user.boundary.primary

data class AuthenticateIdentityCommand(
        val username: String,
        val password: String
)
