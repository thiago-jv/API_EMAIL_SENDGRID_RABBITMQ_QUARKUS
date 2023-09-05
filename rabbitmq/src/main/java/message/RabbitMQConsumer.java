package message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.dto.PedidoDTO;
import domain.entity.PedidoEntity;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RabbitMQConsumer {

    private final ReactiveMailer mailer;

    public RabbitMQConsumer(ReactiveMailer mailer) {
        this.mailer = mailer;
    }

    @Incoming("pedido-queue")
    public Uni<Void> pedidoConsumer(Message<String> message) throws JsonProcessingException {
        Log.info("Consumer payload recebido -> " + message.getPayload());
        Mail mail = Mail.withText(
                "thiago.henrique.25@hotmail.com",
                "Mail enviado do app-quarkus",
                "Ola Thiago, seu email de teste foi enviado com sucesso");

        ObjectMapper objectMapper = new ObjectMapper();
        PedidoDTO pedido = objectMapper.readValue(message.getPayload(), PedidoDTO.class);
        var entity = PedidoEntity.builder()
                .id(pedido.idPedido)
                .horario(pedido.getHorario())
                .valorTotal(pedido.getValorTotal())
                .cliente(pedido.getCliente())
                .build();

        return entity.persist(entity).flatMap(unused -> mailer.send(mail).replaceWithVoid()
                .call(() -> ack(message))
                .onFailure()
                .call(t -> nack(message, t)));
    }

    private Uni<Void> ack(Message<String> message) {
        Log.info("Consumido com sucesso pedido-queue");
        return Uni.createFrom().completionStage(message.ack()).replaceWithVoid();
    }

    private Uni<Void> nack(Message<String> message, Throwable throwable) {
        Log.error("Falha ao consumir pedido-queue", throwable);
        return Uni.createFrom().completionStage(message.nack(new Exception(throwable))).replaceWithVoid();
    }

}
