package pl.pollub.bsi.user.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.GetUserDetailsQuery
import pl.pollub.bsi.user.boundary.GetUserDetailsQueryResponse
import pl.pollub.bsi.user.boundary.IdentityRepository
import pl.pollub.bsi.user.boundary.UserError
import pl.pollub.bsi.user.boundary.primary.Password
import pl.pollub.bsi.user.boundary.primary.Passwords
import pl.pollub.bsi.wallet.boundary.GetWalletUserQuery
import pl.pollub.bsi.wallet.domain.WalletFacade
import pl.pollub.bsi.user.infrastructure.PasswordEncrypter

@Context
class GetUserDetailsQueryHandler(private val identityRepository: IdentityRepository,
                                 private val walletFacade: WalletFacade) {
    fun handle(query: GetUserDetailsQuery): Either<UserError, GetUserDetailsQueryResponse> =
            identityRepository.getById(query.id)
                    .filter { it.username == query.username }
                    .map { identity ->
                        GetUserDetailsQueryResponse(
                                identity.userId,
                                identity.username,
                                identity.password,
                                identity.chosenAlgorithm,
                                identity.salt,
                                Passwords(walletFacade.getWallet(GetWalletUserQuery(identity.userId)).passwords
                                        .map { Password(it.username, it.password, it.url, it.description) }
                                        .map { showPassword(query.passwordHidden, it, identity.password) }
                                )
                        )
                    }
                    .map { Either.right<UserError, GetUserDetailsQueryResponse>(it) }
                    .getOrElse { Either.left(UserError("User not found.")) }

    private fun showPassword(passwordHidden: Boolean, password: Password, masterPassword: String): Password {
        if (!passwordHidden) {
            return password.withPassword(PasswordEncrypter.AES.decrypt(password.password, masterPassword))
        }
        return password
    }
}
