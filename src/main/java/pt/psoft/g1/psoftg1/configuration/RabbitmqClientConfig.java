package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.usermanagement.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.users");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_User_Created")
        public Queue autoDeleteQueue_User_Created() {
            System.out.println("autoDeleteQueue_Book_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_User_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_User_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_User_Created") Queue autoDeleteQueue_User_Created) {
            return BindingBuilder.bind(autoDeleteQueue_User_Created)
                    .to(direct)
                    .with(UserEvents.USER_CREATED);
        }

//        @Bean
//        public Binding binding2(DirectExchange direct,
//                                Queue autoDeleteQueue_User_Updated) {
//            return BindingBuilder.bind(autoDeleteQueue_User_Updated)
//                    .to(direct)
//                    .with(UserEvents.USER_UPDATED);
//        }
//
//        @Bean
//        public Binding binding3(DirectExchange direct,
//                                Queue autoDeleteQueue_User_Deleted) {
//            return BindingBuilder.bind(autoDeleteQueue_User_Deleted)
//                    .to(direct)
//                    .with(UserEvents.USER_DELETED);
//        }

        @Bean
        public UserEventRabbitmqReceiver receiver(UserService userService, @Qualifier("autoDeleteQueue_User_Created") Queue autoDeleteQueue_User_Created) {
            return new UserEventRabbitmqReceiver(userService);
        }
    }
}
