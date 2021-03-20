package pl.pollub.bsi.user.domain

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import io.vavr.collection.List
import io.vavr.control.Either
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import pl.pollub.bsi.user.boundary.Algorithm
import pl.pollub.bsi.user.boundary.UserError
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.CreateUserCommandSuccess
import pl.pollub.bsi.user.boundary.primary.Password
import pl.pollub.bsi.user.boundary.primary.Passwords
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
@Testcontainers
class CreateUserCommandHandlerSpec extends Specification implements TestPropertyProvider {
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")


    @Inject
    CreateUserCommandHandler commandHandler


    def "should create user"() {
        given:
        def command = new CreateUserCommand("username", "password", Algorithm.HMAC, new Passwords(List.of(new Password(
                "user", "pass", "url", "desc"
        ))))

        when:
        def handle = commandHandler.handle(command)

        then:
        handle.isRight()
    }

    def "should not create user and return error that user already exists"() {
        given:
        def command = new CreateUserCommand("username", "password", Algorithm.HMAC, new Passwords(List.of(new Password(
                "user", "pass", "url", "desc"
        ))))

        when:
        def handle = commandHandler.handle(command)
        def handle1 = commandHandler.handle(command)

        then:
        handle.isLeft()
    }

    @Override
    Map<String, String> getProperties() {
        return Map.of(
                "PG_HOST", postgreSQLContainer.containerIpAddress,
                "PG_PORT", postgreSQLContainer.getMappedPort(5432).toString()
        )
    }
}
