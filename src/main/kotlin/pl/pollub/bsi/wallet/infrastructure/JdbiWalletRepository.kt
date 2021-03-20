package pl.pollub.bsi.wallet.infrastructure

import io.micronaut.context.annotation.Context
import io.vavr.collection.List
import io.vavr.control.Option
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.inTransactionUnchecked
import org.jdbi.v3.core.kotlin.useHandleUnchecked
import org.jdbi.v3.core.kotlin.useTransactionUnchecked
import pl.pollub.bsi.wallet.boundary.*

@Context
class JdbiWalletRepository(private val jdbi: Jdbi) : WalletRepository {
    override fun getWalletByUserId(query: GetWalletUserQuery): List<Password> {
        val passwords = jdbi.inTransactionUnchecked { handle ->
            handle.createQuery("SELECT * FROM password WHERE USERID = :id AND DELETED = :deleted")
                    .bind("id", query.userId)
                    .bind("deleted", false)
                    .mapTo(Password::class.java)
                    .list()
        }
        return List.ofAll(passwords)
    }

    override fun getPasswordById(passwordId: Long) : Option<Password> {
        val password = jdbi.inTransactionUnchecked { handle ->
            handle.createQuery("SELECT * FROM password WHERE ID = :id AND DELETED = :deleted")
                    .bind("id", passwordId)
                    .bind("deleted", false)
                    .mapTo(Password::class.java)
                    .findFirst()
        }

        return Option.ofOptional(password)
    }

    override fun save(command: CreateWalletCommand): CreateWalletCommandResponse {
        command.passwords.forEach {
            jdbi.useHandleUnchecked {
                handle -> handle.createUpdate(
                    "INSERT INTO password (USERNAME, PASSWORD, URL, DESCRIPTION, USERID) VALUES (:username, :password, :url, :description, :userId)")
                    .bind("username", it.username)
                    .bind("password", it.password)
                    .bind("url", it.url)
                    .bind("description", it.description)
                    .bind("userId", it.userId)
                    .execute()
            }
        }
        return CreateWalletCommandResponse("Passwords created.")
    }

    override fun update(wallet: List<Password>): UpdatePasswordsCommandResponse {
        wallet.forEach {
            jdbi.useHandleUnchecked {
                handle -> handle.createUpdate(
                    "UPDATE password SET ID = :id, USERNAME = :username, PASSWORD = :password, URL = :url, DESCRIPTION = :description, USERID = :userId WHERE ID = :id")
                    .bind("id", it.id)
                    .bind("username", it.username)
                    .bind("password", it.password)
                    .bind("url", it.url)
                    .bind("description", it.description)
                    .bind("userId", it.userId)
                    .execute()
            }
        }
        return UpdatePasswordsCommandResponse("Passwords updated.")
    }

    override fun delete(password: Password): DeletePasswordCommandResponse {
        jdbi.useHandleUnchecked {
            handle -> handle.createUpdate(
                "UPDATE password SET ID = :id, USERNAME = :username, PASSWORD = :password, URL = :url, DESCRIPTION = :description, USERID = :userId , DELETED = :deleted WHERE ID = :id")
                .bind("id", password.id)
                .bind("username", password.username)
                .bind("password", password.password)
                .bind("url", password.url)
                .bind("description", password.description)
                .bind("userId", password.userId)
                .bind("deleted", true)
                .execute()
        }

        return DeletePasswordCommandResponse("Password deleted.")
    }
}
