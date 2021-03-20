package pl.pollub.bsi.user.boundary

import io.vavr.control.Option
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.GetIdentityByUsernameQuery
import pl.pollub.bsi.user.boundary.primary.Identity
import java.util.*

interface IdentityRepository {
    fun save(command: CreateUserCommand, salt: UUID): CreateUserCommandResult
    fun existsByUsername(username: String): Boolean
    fun getById(id: UUID): Option<Identity>
    fun getByUsername(query: GetIdentityByUsernameQuery): Option<Identity>
    fun update(identity: Identity): Identity
}
