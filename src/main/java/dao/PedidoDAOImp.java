package dao;

import config.ConexionDB;
import entities.Pedido;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// =========================================================================
// VERSIÓN 1: BÁSICA / DIRECTA (ACTIVA)
// =========================================================================
public class PedidoDAOImp implements PedidoDAO {

    @Override
    public void insertarPedido(Pedido pedido) {
        String sqlInsert = "INSERT INTO pedidos (fecha_pedido, cantidad, id_producto) VALUES (?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        Connection conn = null;
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtUpdate = null;

        try {
            conn = ConexionDB.conectar();
            conn.setAutoCommit(false); // Inicia transacción

            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setDate(1, pedido.getFecha_pedido());
            pstmtInsert.setInt(2, pedido.getCantidad());
            pstmtInsert.setInt(3, pedido.getId_producto());
            pstmtInsert.executeUpdate();

            pstmtUpdate = conn.prepareStatement(sqlUpdateStock);
            pstmtUpdate.setInt(1, pedido.getCantidad());
            pstmtUpdate.setInt(2, pedido.getId_producto());
            pstmtUpdate.executeUpdate();

            conn.commit(); // Confirma transacción
            System.out.println("Pedido registrado con éxito.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Deshace si hay error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (pstmtInsert != null) pstmtInsert.close();
                if (pstmtUpdate != null) pstmtUpdate.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Pedido> listarPedidos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT p.id_pedido, p.fecha_pedido, p.cantidad, p.id_producto, pr.nombre " +
                "FROM pedidos p " +
                "INNER JOIN productos pr ON p.id_producto = pr.id_producto";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Pedido ped = new Pedido();
                ped.setId_pedido(rs.getInt("id_pedido"));
                ped.setFecha_pedido(rs.getDate("fecha_pedido"));
                ped.setCantidad(rs.getInt("cantidad"));
                ped.setId_producto(rs.getInt("id_producto"));
                ped.setNombre_producto(rs.getString("nombre"));
                lista.add(ped);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los pedidos: " + e.getMessage());
        }
        return lista;
    }
}

/*
=========================================================================
VERSIÓN 2: AVANZADA / ESTILO PROFESOR (COMENTADA PARA REFERENCIA)
=========================================================================
public class PedidoDAOImpl implements PedidoDAO {

    private final Connection connection;

    public PedidoDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertarPedido(Pedido pedido) {
        String sqlInsert = "INSERT INTO pedidos (fecha_pedido, cantidad, id_producto) VALUES (?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock = stock - ? WHERE id_producto = ?";

        // Al usar conexión inyectada, el commit/rollback y el cierre de la conexión
        // normalmente se manejan desde fuera (en el Main). Aquí solo definimos la lógica.
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement stmtInsert = connection.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                stmtInsert.setDate(1, pedido.getFecha_pedido());
                stmtInsert.setInt(2, pedido.getCantidad());
                stmtInsert.setInt(3, pedido.getId_producto());
                stmtInsert.executeUpdate();

                try (ResultSet rs = stmtInsert.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId_pedido(rs.getInt(1));
                    }
                }
            }

            try (PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdateStock)) {
                stmtUpdate.setInt(1, pedido.getCantidad());
                stmtUpdate.setInt(2, pedido.getId_producto());
                stmtUpdate.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    @Override
    public List<Pedido> listarPedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedidos";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pedidos.add(new Pedido(
                        rs.getInt("id_pedido"),
                        rs.getDate("fecha_pedido"),
                        rs.getInt("cantidad"),
                        rs.getInt("id_producto")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pedidos;
    }
}
*/