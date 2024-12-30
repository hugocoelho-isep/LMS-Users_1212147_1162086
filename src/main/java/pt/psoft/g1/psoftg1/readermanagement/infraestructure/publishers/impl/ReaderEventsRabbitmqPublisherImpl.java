package pt.psoft.g1.psoftg1.readermanagement.infraestructure.publishers.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Locked;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQP;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewAMQPMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.publishers.ReaderEventsPublisher;
import pt.psoft.g1.psoftg1.shared.model.ReaderEvents;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQP;
import pt.psoft.g1.psoftg1.usermanagement.api.UserViewAMQPMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

@Service
@RequiredArgsConstructor
public class ReaderEventsRabbitmqPublisherImpl implements ReaderEventsPublisher {

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private DirectExchange direct;

    private final ReaderViewAMQPMapper readerViewAMQPMapper;

    @Override
    public void sendReaderCreated(ReaderDetails readerDetails) {
        sendReaderEvent(readerDetails, readerDetails.getVersion(), ReaderEvents.READER_CREATED);
    }

    @Override
    public void sendReaderUpdated(ReaderDetails updatedReader, long currentVersion) {
        sendReaderEvent(updatedReader, currentVersion, ReaderEvents.READER_UPDATED);
    }

    private void sendReaderEvent(ReaderDetails readerDetails, Long currentVersion, String readerEventType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ReaderViewAMQP readerViewAMQP = readerViewAMQPMapper.toReaderViewAMQP(readerDetails, readerDetails.getReader());
            readerViewAMQP.setVersion(currentVersion);

            String jsonString = objectMapper.writeValueAsString(readerViewAMQP);

            this.template.convertAndSend(direct.getName(), readerEventType, jsonString);

            System.out.println(" [x] Sent '" + jsonString + "'");
        }
        catch( Exception ex ) {
            System.out.println(" [x] Exception sending reader event: '" + ex.getMessage() + "'");
        }
    }

}
