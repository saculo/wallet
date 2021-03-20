package pl.pollub.bsi.user.boundary.primary

data class AuthenticateError(private val message: String) {
    fun message() = message
}
