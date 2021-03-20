package pl.pollub.bsi.user.domain

import io.micronaut.context.annotation.Context
import io.vavr.control.Either
import pl.pollub.bsi.user.boundary.IdentityRepository
import pl.pollub.bsi.user.boundary.UserError
import pl.pollub.bsi.user.boundary.primary.Identity
import java.util.*

@Context
class UserFacade(private val identityRepository: IdentityRepository) {
    fun userById(id: UUID) : Either<UserError, Identity> = identityRepository.getById(id).toEither(UserError("User not found."))
}
