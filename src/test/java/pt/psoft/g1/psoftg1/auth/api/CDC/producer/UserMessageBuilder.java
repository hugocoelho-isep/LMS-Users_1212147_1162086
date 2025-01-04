package pt.psoft.g1.psoftg1.auth.api.CDC.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;

public class UserMessageBuilder {
    private ObjectMapper mapper = new ObjectMapper();
    private UserViewAMQP userViewAMQP;

    public UserMessageBuilder withUser(UserViewAMQP userViewAMQP) {
        this.userViewAMQP = userViewAMQP;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.userViewAMQP))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}