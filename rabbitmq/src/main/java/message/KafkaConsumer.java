package message;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaConsumer {

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    @Incoming("pedido")
    public CompletionStage<Void> consumerPedido(Message<String> message) {
        LOG.info("-- Recebendo Novo Pedido via topico KAFKA--" + message.getPayload());
        return message.ack();
    }
}