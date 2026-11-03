package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Pedido {
    private Integer id_pedido;
    private Date fecha_pedido;
    private Integer cantidad;
    private Integer id_producto;
    private String nombre_producto;

}
