package pl.pollub.bsi.user.boundary

data class UpdatePasswordCommand(
        val password: String,
        val algorithm: Algorithm
) {

}
