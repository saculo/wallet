package pl.pollub.bsi.user.infrastructure

import io.micronaut.context.annotation.Context
import io.vavr.control.Option
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import pl.pollub.bsi.user.boundary.CreateUserCommandResult
import pl.pollub.bsi.user.boundary.IdentityRepository
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.GetIdentityByUsernameQuery
import pl.pollub.bsi.user.boundary.primary.Identity
import java.util.*

@Context
class JdbiIdentityRepository(private val jdbi: Jdbi) : IdentityRepository {
    override fun getByUsername(query: GetIdentityByUsernameQuery): Option<Identity> {
        val identity = jdbi.inTransactionUnchecked { handle ->
            handle.createQuery("SELECT * FROM identity WHERE username = :username")
                    .bind("username", query.username)
                    .mapTo(Identity::class.java)
                    .findFirst()
        }
        return Option.ofOptional(identity)
    }

    override fun save(command: CreateUserCommand, salt: UUID): CreateUserCommandResult {
        val userId = UUID.randomUUID()
        val result = jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                    "INSERT INTO identity (USERID, USERNAME, PASSWORD, CHOSENALGORITHM, SALT) VALUES (:id, :username, :password, :algorithm, :salt)")
                    .bind("id", userId)
                    .bind("username", command.username)
                    .bind("password", command.password)
                    .bind("algorithm", command.algorithm.type)
                    .bind("salt", salt)
                    .execute()
        }

        return CreateUserCommandResult("User created.", userId)

    }

    override fun existsByUsername(username: String): Boolean =
            jdbi.inTransactionUnchecked { handle ->
                handle.createQuery("SELECT * FROM identity WHERE username = :username")
                        .bind("username", username)
                        .mapTo(Identity::class.java)
                        .findFirst()
                        .map { true }
                        .orElse(false)
            }

    override fun getById(id: UUID): Option<Identity> {
        val identity = jdbi.inTransactionUnchecked { handle ->
            handle.createQuery("SELECT * FROM identity WHERE USERID = :id")
                    .bind("id", id)
                    .mapTo(Identity::class.java)
                    .findFirst()
        }
        return Option.ofOptional(identity)
    }

    override fun update(identity: Identity): Identity {
        val result = jdbi.useHandleUnchecked { handle ->
            handle.createUpdate(
                    "UPDATE identity SET ID = :id, USERID = :userId, USERNAME = :username, PASSWORD = :password, CHOSENALGORITHM = :algorithm, SALT = :salt WHERE ID = :id")
                    .bind("id", identity.id)
                    .bind("userId", identity.userId)
                    .bind("username", identity.username)
                    .bind("password", identity.password)
                    .bind("algorithm", identity.chosenAlgorithm.type)
                    .bind("salt", identity.salt)
                    .execute()
        }

        return identity

    }
}
