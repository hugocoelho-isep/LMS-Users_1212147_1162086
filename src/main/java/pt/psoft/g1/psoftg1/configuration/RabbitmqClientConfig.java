package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.lendingmanagement.api.LendingEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.lendingmanagement.services.LendingService;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.LendingEvents;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import pt.psoft.g1.psoftg1.usermanagement.api.UserEventRabbitmqReceiver;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean(name = "directExchangeUsers")
    public DirectExchange direct() {
        return new DirectExchange("LMS.users");
    }

    @Bean(name = "directExchangeLendings")
    public DirectExchange directLendings() {
        return new DirectExchange("LMS.lendings");
    }

    private static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_User_Created")
        public Queue autoDeleteQueue_User_Created() {
            System.out.println("autoDeleteQueue_User_Created created!");
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
        public Binding binding1(@Qualifier("directExchangeUsers") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_User_Created") Queue autoDeleteQueue_User_Created) {
            return BindingBuilder.bind(autoDeleteQueue_User_Created)
                    .to(direct)
                    .with(UserEvents.USER_CREATED);
        }

        @Bean
        public Binding binding2(@Qualifier("directExchangeUsers") DirectExchange direct,
                                Queue autoDeleteQueue_User_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_User_Updated)
                    .to(direct)
                    .with(UserEvents.USER_UPDATED);
        }

        @Bean
        public Binding binding3(@Qualifier("directExchangeUsers") DirectExchange direct,
                                Queue autoDeleteQueue_User_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_User_Deleted)
                    .to(direct)
                    .with(UserEvents.USER_DELETED);
        }

        @Bean(name = "UserEventRabbitmqReceiver")
        public UserEventRabbitmqReceiver receiver(UserService userService, @Qualifier("autoDeleteQueue_User_Created") Queue autoDeleteQueue_User_Created) {
            return new UserEventRabbitmqReceiver(userService);
        }

        /* ------- READER ------- */

        @Bean(name = "autoDeleteQueue_Reader_Created")
        public Queue autoDeleteQueue_Reader_Created() {
            System.out.println("autoDeleteQueue_Reader_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Reader_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding4(@Qualifier("directExchangeUsers") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Created)
                    .to(direct)
                    .with(ReaderEvents.READER_CREATED);
        }

        @Bean
        public Binding binding5(@Qualifier("directExchangeUsers") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Reader_Updated") Queue autoDeleteQueue_Reader_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Reader_Updated)
                    .to(direct)
                    .with(ReaderEvents.READER_UPDATED);
        }

        @Bean(name = "ReaderEventRabbitmqReceiver")
        public ReaderEventRabbitmqReceiver receiver(ReaderService ReaderService, @Qualifier("autoDeleteQueue_Reader_Created") Queue autoDeleteQueue_Reader_Created) {
            return new ReaderEventRabbitmqReceiver(ReaderService);
        }


        /* ------- LENDING ------- */

        @Bean(name = "autoDeleteQueue_Lending_Created")
        public Queue autoDeleteQueue_Lending_Created() {
            System.out.println("autoDeleteQueue_Lending_Created created!");
            return new AnonymousQueue();
        }

        @Bean(name = "autoDeleteQueue_Lending_Updated")
        public Queue autoDeleteQueue_Lending_Updated() {
            System.out.println("autoDeleteQueue_Lending_Updated updated!");
            return new AnonymousQueue();
        }


        @Bean
        public Binding binding6(@Qualifier("directExchangeLendings") DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Lending_Created") Queue autoDeleteQueue_Lending_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Created)
                    .to(direct)
                    .with(LendingEvents.LENDING_CREATED);
        }

        @Bean
        public Binding binding7(@Qualifier("directExchangeLendings") DirectExchange direct,
                                Queue autoDeleteQueue_Lending_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Lending_Updated)
                    .to(direct)
                    .with(LendingEvents.LENDING_UPDATED);
        }

        @Bean(name = "LendingEventRabbitmqReceiver")
        public LendingEventRabbitmqReceiver receiver(LendingService lendingService, @Qualifier("autoDeleteQueue_Lending_Created") Queue autoDeleteQueue_Lending_Created) {
            return new LendingEventRabbitmqReceiver(lendingService);
        }

    }
}
