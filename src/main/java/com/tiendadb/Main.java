package com.tiendadb;

import dao.PedidoDAO;
import dao.PedidoDAOImp;
import dao.ProductoDAO;
import dao.ProductoDAOImp;
import entities.Pedido;
import entities.Producto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ProductoDAO productoDAO = new ProductoDAOImp();
        PedidoDAO pedidoDAO = new PedidoDAOImp();
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- MENÚ TIENDA ---");
            System.out.println("1. Insertar producto");
            System.out.println("2. Consultar producto por id");
            System.out.println("3. Actualizar stock de producto");
            System.out.println("4. Insertar pedido");
            System.out.println("5. Listado de pedidos");
            System.out.println("6. Eliminar un producto");
            System.out.println("7. Salir");
            System.out.print("Elige una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Nombre del producto (sin espacios): ");
                    String nombre = scanner.next();
                    System.out.print("Precio (usa coma para decimales): ");
                    BigDecimal precio = scanner.nextBigDecimal();
                    System.out.print("Stock inicial: ");
                    int stock = scanner.nextInt();
                    productoDAO.insertarProducto(new Producto(null, nombre, precio, stock));
                    break;

                case 2:
                    System.out.print("ID del producto a buscar: ");
                    Producto p = productoDAO.consultarId(scanner.nextInt());
                    if (p != null) {
                        System.out.println("=> " + p.getNombre() + " | Precio: " + p.getPrecio() + " | Stock: " + p.getStock());
                    } else {
                        System.out.println("=> Producto no encontrado.");
                    }
                    break;

                case 3:
                    System.out.print("ID del producto a actualizar: ");
                    int idActualizar = scanner.nextInt();
                    System.out.print("Nuevo stock: ");
                    int nuevoStock = scanner.nextInt();
                    productoDAO.actualizarStock(idActualizar, nuevoStock);
                    break;

                case 4:
                    System.out.print("ID del producto que quieres comprar: ");
                    int idProd = scanner.nextInt();
                    System.out.print("Cantidad a comprar: ");
                    int cantidad = scanner.nextInt();

                    Producto prod = productoDAO.consultarId(idProd);
                    if (prod == null) {
                        System.out.println("El producto no existe.");
                    } else if (prod.getStock() < cantidad) {
                        System.out.println("No hay stock suficiente. Stock actual: " + prod.getStock());
                    } else {
                        pedidoDAO.insertarPedido(new Pedido(null, new Date(System.currentTimeMillis()), cantidad, idProd, null));
                    }
                    break;

                case 5:
                    System.out.println("\n--- LISTADO DE PEDIDOS ---");
                    List<Pedido> pedidos = pedidoDAO.listarPedidos();
                    if (pedidos.isEmpty()) {
                        System.out.println("Aún no hay pedidos registrados.");
                    } else {
                        for (Pedido ped : pedidos) {
                            System.out.println("Pedido ID: " + ped.getId_pedido() +
                                    " | Fecha: " + ped.getFecha_pedido() +
                                    " | Cantidad: " + ped.getCantidad() +
                                    " | Producto: " + ped.getNombre_producto() + " (ID: " + ped.getId_producto() + ")");
                        }
                    }
                    break;

                case 6:
                    System.out.print("ID del producto a eliminar: ");
                    int idEliminar = scanner.nextInt();
                    productoDAO.eliminarProducto(idEliminar);
                    break;

                case 7:
                    System.out.println("Saliendo de la aplicación...");
                    break;

                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 7);

        scanner.close();
    }
}