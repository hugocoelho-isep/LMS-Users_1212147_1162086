package pt.psoft.g1.psoftg1.auth.api.CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {UserEventRabbitmqReceiver.class, UserService.class}
)
@PactConsumerTest
@PactTestFor(providerName = "user_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class UsersCDCDefinitionTest {

    @MockBean
    UserService userService;

    @Autowired
    UserEventRabbitmqReceiver listener;

    @Pact(consumer = "user_created-consumer")
    V4Pact createUserCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody();
        body.stringType("username", "joaquim@gmail.com");
        body.stringType("password", "Joaquimpassword!123");
        body.stringType("name", "Joaquim Silva");
        body.stringMatcher("role", "READER");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a user created event").withMetadata(metadata).withContent(body).toPact();
    }

    @Pact(consumer = "user_updated-consumer")
    V4Pact createUserUpdatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("username", "tiago@gmail.com")
                .stringType("password", "Tiagopassword!123")
                .stringType("name", "Tiago Silva")
                .stringMatcher("role", "LIBRARIAN");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("a user updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createUserCreatedPact")
    void testUserCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
//        // Convert the Pact message to a String (JSON payload)
//        String jsonReceived = messages.get(0).contentsAsString();
//
//        // Create a Spring AMQP Message with the JSON payload and optional headers
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/json");
//        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);
//
//        // Simulate receiving the message in the listener
//        assertDoesNotThrow(() -> {
//            listener.receiveUserCreated(message);
//        });
//
//        // Verify interactions with the mocked service
//        verify(userService, times(1)).create(any(UserViewAMQP.class));
    }

    @Test
    @PactTestFor(pactMethod = "createUserUpdatedPact")
    void testUserUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
//        String jsonReceived = messages.get(0).contentsAsString();
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setContentType("application/json");
//        org.springframework.amqp.core.Message message = new org.springframework.amqp.core.Message(jsonReceived.getBytes(StandardCharsets.UTF_8), messageProperties);
//
//        assertDoesNotThrow(() -> {
//            listener.receiveUserUpdated(message);
//        });
//
//        // Verify interactions with the mocked service
//        verify(userService, times(1)).update(any(UserViewAMQP.class));
    }
}