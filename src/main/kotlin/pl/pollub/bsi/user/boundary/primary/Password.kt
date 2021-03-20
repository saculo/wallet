package pl.pollub.bsi.user.boundary.primary

data class Password (
    val username: String,
    val password: String,
    val url: String,
    val description: String
) {
    fun withPassword(password: String) : Password = Password(this.username, password, this.url, this.description)
}
