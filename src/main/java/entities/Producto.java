package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Producto  {
    private Integer id_producto;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;

}
