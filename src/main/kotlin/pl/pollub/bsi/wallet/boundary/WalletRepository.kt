package pl.pollub.bsi.wallet.boundary

import io.vavr.collection.List
import io.vavr.control.Option

interface WalletRepository {
    fun getWalletByUserId(query : GetWalletUserQuery) : List<Password>
    fun getPasswordById(passwordId: Long) : Option<Password>
    fun save(command: CreateWalletCommand) : CreateWalletCommandResponse
    fun update(wallet: List<Password>): UpdatePasswordsCommandResponse
    fun delete(password: Password) : DeletePasswordCommandResponse
}
