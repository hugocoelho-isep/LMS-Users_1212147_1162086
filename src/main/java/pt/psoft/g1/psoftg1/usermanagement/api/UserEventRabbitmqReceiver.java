package pt.psoft.g1.psoftg1.usermanagement.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;
import org.springframework.amqp.core.Message;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class UserEventRabbitmqReceiver {

        private final UserService userService;

        @RabbitListener(queues = "#{autoDeleteQueue_User_Created.name}")
        public void receiveUserCreated(Message msg) {

            try {
                ObjectMapper objectMapper = new ObjectMapper();

                String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
                UserViewAMQP userViewAMQP = objectMapper.readValue(jsonReceived, UserViewAMQP.class);

                System.out.println(" [x] Received User Created by AMQP: " + msg + ".");
                try {
                    userService.create(userViewAMQP);
                    System.out.println(" [x] New user inserted from AMQP: " + msg + ".");
                } catch (Exception e) {
                    System.out.println(" [x] User already exists. No need to store it.");
                }
            }
            catch(Exception ex) {
                System.out.println(" [x] Exception receiving user event from AMQP: '" + ex.getMessage() + "'");
            }
        }

//        @RabbitListener(queues = "#{autoDeleteQueue_User_Updated.name}")
//        public void receiveUserUpdated(Message msg) {
//            try {
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
//                UserViewAMQP userViewAMQP = objectMapper.readValue(jsonReceived, UserViewAMQP.class);
//
//                System.out.println(" [x] Received User Updated by AMQP: " + msg + ".");
//                try {
//                    userService.update(userViewAMQP);
//                    System.out.println(" [x] User updated from AMQP: " + msg + ".");
//                } catch (Exception e) {
//                    System.out.println(" [x] User does not exists or wrong version. Nothing stored.");
//                }
//            }
//            catch(Exception ex) {
//                System.out.println(" [x] Exception receiving user event from AMQP: '" + ex.getMessage() + "'");
//            }
//        }

//        @RabbitListener(queues = "#{autoDeleteQueue_User_Deleted.name}")
//        public void receiveUserDeleted(Message msg) {
//            try {
//                ObjectMapper objectMapper = new ObjectMapper();
//
//                String jsonReceived = new String(msg.getBody(), StandardCharsets.UTF_8);
//                UserViewAMQP userViewAMQP = objectMapper.readValue(jsonReceived, UserViewAMQP.class);
//
//                System.out.println(" [x] Received User Deleted by AMQP: " + msg + ".");
//                try {
//                    userService.delete(userViewAMQP);
//                    System.out.println(" [x] User deleted from AMQP: " + msg + ".");
//                } catch (Exception e) {
//                    System.out.println(" [x] User does not exists or wrong version. Nothing stored.");
//                }
//            }
//            catch(Exception ex) {
//                System.out.println(" [x] Exception receiving user event from AMQP: '" + ex.getMessage() + "'");
//            }
//        }
}
