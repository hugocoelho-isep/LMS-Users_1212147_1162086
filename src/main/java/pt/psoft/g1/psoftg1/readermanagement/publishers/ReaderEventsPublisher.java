package pt.psoft.g1.psoftg1.readermanagement.publishers;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

public interface ReaderEventsPublisher {
    void sendReaderCreated(ReaderDetails readerDetails);

    void sendReaderUpdated(ReaderDetails readerDetails, long currentVersion);
}
