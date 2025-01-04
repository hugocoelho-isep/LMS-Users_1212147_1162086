package pt.psoft.g1.psoftg1.auth.api.CDC.producer;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQPMapperImpl;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.publishers.impl.UserEventsRabbitmqPublisherImpl;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.publishers.UserEventsPublisher;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.HashMap;

@Import(UsersProducerCDCIT.TestConfig.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@Provider("user_event-producer")
@PactFolder("target/pacts")
public class UsersProducerCDCIT {
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersProducerCDCIT.class);

    @MockBean
    RabbitTemplate template;

    @MockBean (name = "userEventsExchange")
    DirectExchange direct;

    @MockBean
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @Autowired
    UserEventsPublisher userEventsPublisher;

    @Autowired
    UserViewAMQPMapperImpl userViewAMQPMapper;

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void testTemplate(Pact pact, Interaction interaction, PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new MessageTestTarget());
    }

    @PactVerifyProvider("a user created event")
    public MessageAndMetadata userCreated() throws JsonProcessingException {
        User user = User.newUser("joaquim@gmail.com", "Joaquimpassword!123", "Joaquim Silva", "READER");

        // Use mocks to simulate behavior
        UserViewAMQP userViewAMQP = new UserViewAMQP(1L, "joaquim@gmail.com", 1L, "READER", "Joaquim Silva", "READER");
        Message<String> message = new UserMessageBuilder().withUser(userViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    @PactVerifyProvider("a user updated event")
    public MessageAndMetadata userUpdated() throws JsonProcessingException {
        User user = User.newUser("tiago@gmail.com", "Tiagopassword!123", "Tiago Silva", "LIBRARIAN");

        // Use mocks to simulate behavior

        UserViewAMQP userViewAMQP = new UserViewAMQP(2L, "tiago@gmail.com", 1L, "LIBRARIAN", "Tiago Silva", "LIBRARIAN");
        Message<String> message = new UserMessageBuilder().withUser(userViewAMQP).build();

        return generateMessageAndMetadata(message);
    }

    private MessageAndMetadata generateMessageAndMetadata(Message<String> message) {
        HashMap<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach((k, v) -> metadata.put(k, v));

        return new MessageAndMetadata(message.getPayload().getBytes(), metadata);
    }

    @Configuration
    static class TestConfig {
        @Bean
        public UserEventsPublisher userEventsPublisher(UserViewAMQPMapperImpl userViewAMQPMapper) {
            return new UserEventsRabbitmqPublisherImpl(userViewAMQPMapper);
        }

        @Bean
        public UserViewAMQPMapperImpl userViewAMQPMapper() {
            return new UserViewAMQPMapperImpl();
        }

        @Bean
        public DirectExchange directExchangeUsers() {
            return new DirectExchange("directExchangeUsers");
        }
    }
}
