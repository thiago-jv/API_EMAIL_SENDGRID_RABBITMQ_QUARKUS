package domain.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import lombok.Builder;
import lombok.Data;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Builder
@Data
@MongoEntity(collection = "pedido")
public class PedidoEntity extends ReactivePanacheMongoEntityBase {

    @BsonId
    @BsonProperty("_id")
    public String id;

    private String horario;

    private String valorTotal;

    private String cliente;
}
