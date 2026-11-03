package dao;

import config.ConexionDB;
import entities.Producto;
import java.sql.*;


public class ProductoDAOImp implements ProductoDAO {

    @Override

    public void insertarProducto(Producto producto) {
        String sql = "INSERT INTO productos (nombre, precio, stock) VALUES (?, ?, ?)";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, producto.getNombre());
            pstmt.setBigDecimal(2, producto.getPrecio());
            pstmt.setInt(3, producto.getStock());
            pstmt.executeUpdate();
            System.out.println("Producto insertado con éxito.");

        } catch (SQLException e) {
            System.err.println("Error al insertar producto: " + e.getMessage());
        }
    }


    @Override

    public Producto consultarId(int idProducto) {
        String sql = "SELECT * FROM productos WHERE id_producto = ?";
        Producto producto = null;
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                producto = new Producto();
                producto.setId_producto(rs.getInt("id_producto"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getBigDecimal("precio"));
                producto.setStock(rs.getInt("stock"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar producto: " + e.getMessage());
        }
        return producto;
    }

    @Override
    public void actualizarStock(int idProducto, int nuevoStock) {
        String sql = "UPDATE productos SET stock = ? WHERE id_producto = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, nuevoStock);
            pstmt.setInt(2, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar stock: " + e.getMessage());
        }
    }

    @Override
    public void eliminarProducto(int idProducto) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idProducto);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
    }
}