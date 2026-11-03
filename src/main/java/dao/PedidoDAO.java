package dao;


import entities.Pedido;

import java.util.List;

public interface PedidoDAO {

    // Requisitos  mínimos

    void insertarPedido(Pedido pedido);
    List<Pedido> listarPedidos();


}
