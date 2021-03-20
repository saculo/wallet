package pl.pollub.bsi.wallet.domain

import io.micronaut.context.annotation.Context
import pl.pollub.bsi.wallet.boundary.GetWalletUserQuery
import pl.pollub.bsi.wallet.boundary.GetWalletUserQueryResponse
import pl.pollub.bsi.wallet.boundary.WalletRepository

@Context
class GetWalletQueryHandler(private val walletRepository: WalletRepository) {
    fun getWallet(query: GetWalletUserQuery) : GetWalletUserQueryResponse =
        GetWalletUserQueryResponse(walletRepository.getWalletByUserId(query))
}
