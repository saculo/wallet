package pl.pollub.bsi.wallet.boundary

import io.vavr.collection.List

data class GetWalletUserQueryResponse(
        val passwords: List<Password>
)
