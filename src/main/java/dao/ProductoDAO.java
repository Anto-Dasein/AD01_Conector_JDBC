package dao;

import entities.Producto;

public interface ProductoDAO {
    void insertarProducto(Producto producto);
    Producto consultarId(int idProducto);
    void actualizarStock(int idProducto, int nuevoStock);
    void eliminarProducto(int idProducto);
}