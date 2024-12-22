package pt.psoft.g1.psoftg1.usermanagement.publishers;

import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface UserEventsPublisher {
    void sendUserCreated(User user);
}
