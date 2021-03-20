package pl.pollub.bsi.user.infrastructure

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import pl.pollub.bsi.user.boundary.GetUserDetailsQuery
import pl.pollub.bsi.user.boundary.UpdatePasswordCommand
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.domain.CreateUserCommandHandler
import pl.pollub.bsi.user.domain.GetUserDetailsQueryHandler
import pl.pollub.bsi.user.domain.UpdatePasswordCommandHandler
import java.security.Principal
import java.util.*

@Controller("user")
internal class UserController(private val createUser: CreateUserCommandHandler,
                              private val userDetails: GetUserDetailsQueryHandler,
                              private val updatePassword: UpdatePasswordCommandHandler,
                              private val responseCreator: ResponseCreator) {
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post
    internal fun register(@Body command: CreateUserCommand): HttpResponse<Any> =
            responseCreator.created(createUser.handle(command), HttpStatus.CREATED)

    @Get("{userId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    internal fun details(@PathVariable userId: UUID, passwordHidden: Boolean, principal: Principal): HttpResponse<Any> =
            responseCreator.ok(
                    userDetails.handle(
                            GetUserDetailsQuery(passwordHidden, principal.name, userId)
                    ),
                    HttpStatus.CREATED
            )

    @Put("{userId}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    internal fun updatePassword(@PathVariable userId: UUID, @Body command: UpdatePasswordCommand, principal: Principal): HttpResponse<Any> =
        responseCreator.ok(updatePassword.update(userId, principal.name, command), HttpStatus.OK)
}
