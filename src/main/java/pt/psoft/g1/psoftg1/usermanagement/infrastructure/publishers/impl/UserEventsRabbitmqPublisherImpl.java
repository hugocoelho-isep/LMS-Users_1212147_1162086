package pt.psoft.g1.psoftg1.usermanagement.infrastructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQPMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.publishers.UserEventsPublisher;

@Service
@RequiredArgsConstructor
public class UserEventsRabbitmqPublisherImpl implements UserEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;

    private final UserViewAMQPMapper userViewAMQPMapper;


    @Override
    public void sendUserCreated(User user) {
        sendUserEvent(user, user.getVersion(), UserEvents.USER_CREATED);
    }

    @Override
    public void sendUserUpdated(User user) {
        sendUserEvent(user, user.getVersion(), UserEvents.USER_UPDATED);
    }

    @Override
    public void sendUserDeleted(User user) {
        sendUserEvent(user, user.getVersion(), UserEvents.USER_DELETED);
    }


    public void sendUserEvent(User user, Long currentVersion, String userEventType) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            UserViewAMQP userViewAMQP = userViewAMQPMapper.toUserViewAMQP(user);
            userViewAMQP.setVersion(currentVersion);
            userViewAMQP.setRole(user.getAuthorities().iterator().next().getAuthority());

            String jsonString = objectMapper.writeValueAsString(userViewAMQP);

            this.template.convertAndSend(direct.getName(), userEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending user event: '" + ex.getMessage() + "'");
        }
    }
}
