package domain.dto;

import lombok.Data;

@Data
public class PedidoDTO {

    public String idPedido;

    private String horario;

    private String valorTotal;

    private String cliente;
}
