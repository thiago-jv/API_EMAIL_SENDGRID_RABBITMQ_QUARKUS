package message;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class KafkaConsumer {

    private final Logger LOG = LoggerFactory.getLogger(KafkaConsumer.class);

    // caso ocorra algum problema no consumo, o kafka irá tentar contornar
    // delay = 10 tempo de 10s e tente novamente por maxRetries = 5 vezes
    @Incoming("pedido")
    @Retry(delay = 10, maxRetries = 5, delayUnit = ChronoUnit.SECONDS)
    public CompletionStage<Void> consumerPedido(Message<String> message) {
        LOG.info("-- Recebendo Novo Pedido via topico KAFKA--" + message.getPayload());
        return message.ack();
    }
}