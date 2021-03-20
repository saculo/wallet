package pl.pollub.bsi.wallet.domain

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.vavr.collection.List
import pl.pollub.bsi.user.boundary.Algorithm
import pl.pollub.bsi.user.boundary.primary.CreateUserCommand
import pl.pollub.bsi.user.boundary.primary.Password
import pl.pollub.bsi.user.boundary.primary.Passwords
import pl.pollub.bsi.user.domain.CreateUserCommandHandler
import pl.pollub.bsi.wallet.boundary.CreatePasswordCommand
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class CreatePasswordCommandHandlerTest extends Specification {
    @Inject
    CreateUserCommandHandler userCommandHandler

    @Inject
    CreatePasswordCommandHandler commandHandler


    def "should create password for user"() {
        given:
        def commandUser = new CreateUserCommand("username", "password", Algorithm.HMAC, new Passwords(List.of(new Password(
                "user", "pass", "url", "desc"
        ))))
        def command = new CreatePasswordCommand(
                "user", "pass", "url", "desc"
        )

        when:
        def user = userCommandHandler.handle(commandUser)
        def handle = commandHandler.create(UUID.randomUUID(), "username", command)

        then:
        handle.isRight()
    }

    def "should not create password for user that doesnt exists"() {
        given:
        def commandUser = new CreateUserCommand("username", "password", Algorithm.HMAC, new Passwords(List.of(new Password(
                "user", "pass", "url", "desc"
        ))))
        def command = new CreatePasswordCommand(
                "user", "pass", "url", "desc"
        )

        when:
        def user = userCommandHandler.handle(commandUser)
        def handle = commandHandler.create(UUID.randomUUID(), "username", command)

        then:
        handle.isLeft()
    }
}
