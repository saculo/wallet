package pl.pollub.bsi.wallet.infrastructure

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import pl.pollub.bsi.wallet.boundary.CreatePasswordCommand
import pl.pollub.bsi.wallet.boundary.UpdatePasswordCommand
import pl.pollub.bsi.wallet.domain.CreatePasswordCommandHandler
import pl.pollub.bsi.wallet.domain.DeletePasswordCommandHandler
import pl.pollub.bsi.wallet.domain.PasswordDetailsQueryHandler
import pl.pollub.bsi.wallet.domain.UpdatePasswordCommandHandler
import java.security.Principal
import java.util.*

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("user/{userId}/password")
internal class PasswordController(
        private val createPassword: CreatePasswordCommandHandler,
        private val updatePassword: UpdatePasswordCommandHandler,
        private val deletePassword: DeletePasswordCommandHandler,
        private val details: PasswordDetailsQueryHandler,
        private val creator: ResponseCreator
) {
    @Post
    fun create(@PathVariable userId: UUID, @Body command: CreatePasswordCommand, principal: Principal) =
            creator.created(createPassword.create(userId, principal.name, command), HttpStatus.CREATED)

    @Put("{passwordId}")
    fun update(@PathVariable userId: UUID, @PathVariable passwordId: Long, @Body command: UpdatePasswordCommand, principal: Principal) =
            creator.ok(updatePassword.update(userId, principal.name, passwordId, command), HttpStatus.OK)

    @Delete("{passwordId}")
    fun delete(@PathVariable userId: UUID, @PathVariable passwordId: Long, principal: Principal) =
            creator.ok(deletePassword.delete(userId, principal.name, passwordId), HttpStatus.OK)

    @Get("{passwordId}")
    fun show(@PathVariable userId: UUID, @PathVariable passwordId: Long, passwordHidden: Boolean, principal: Principal) =
            creator.ok(details.showOne(userId, principal.name, passwordId, passwordHidden), HttpStatus.OK)
}
