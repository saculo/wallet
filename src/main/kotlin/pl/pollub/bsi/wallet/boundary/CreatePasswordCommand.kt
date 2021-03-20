package pl.pollub.bsi.wallet.boundary

data class CreatePasswordCommand(
        val username: String,
        val password: String,
        val url: String,
        val description: String
)
