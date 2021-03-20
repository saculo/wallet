package pl.pollub.bsi.user.domain

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.vavr.collection.List
import io.vavr.control.Either
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import pl.pollub.bsi.user.boundary.Algorithm
import pl.pollub.bsi.user.boundary.UpdatePasswordCommand
import pl.pollub.bsi.user.boundary.UpdateWalletCommandResponse
import pl.pollub.bsi.user.boundary.UserError
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.Password
import pl.pollub.bsi.user.boundary.primary.Passwords
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
@Testcontainers
class UpdatePasswordCommandHandlerTest extends Specification {
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")

    @Inject
    CreateUserCommandHandler commandHandler
    @Inject
    UpdatePasswordCommandHandler updatePasswordCommandHandler

    def "should update user password"() {
        given:
        def command = new CreateUserCommand("username", "password", Algorithm.HMAC, new Passwords(List.of(new Password(
                "user", "pass", "url", "desc"
        ))))
        def updateCommand = new UpdatePasswordCommand("newpass", Algorithm.HMAC)

        when:
        def handle = commandHandler.handle(command)
        def update = updatePasswordCommandHandler.update(UUID.randomUUID(), "username", updateCommand)

        then:
        update.isRight()
    }

    @Override
    Map<String, String> getProperties() {
        return Map.of(
                "PG_HOST", postgreSQLContainer.containerIpAddress,
                "PG_PORT", postgreSQLContainer.getMappedPort(5432).toString()
        )
    }
}
