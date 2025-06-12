/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.FuenteIngreso;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuenteIngresoDAO {

    public List<FuenteIngreso> obtenerIngresosPorUsuario(int idUsuario) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<FuenteIngreso> ingresos = new ArrayList<>();

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id_ingreso, descripcion, monto, fecha_ingreso, categoria, observaciones "
                    + "FROM fuentes_ingreso WHERE id_usuario = ?";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                FuenteIngreso ingreso = new FuenteIngreso();
                ingreso.setIdIngreso(resultado.getInt("id_ingreso"));
                ingreso.setIdUsuario(idUsuario);
                ingreso.setDescripcion(resultado.getString("descripcion"));
                ingreso.setMonto(resultado.getDouble("monto"));
                ingreso.setFechaIngreso(resultado.getDate("fecha_ingreso"));
                ingreso.setCategoria(resultado.getString("categoria"));
                ingreso.setObservaciones(resultado.getString("observaciones"));

                ingresos.add(ingreso);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener fuentes de ingreso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultado != null) {
                    resultado.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return ingresos;
    }

    public boolean agregarIngreso(FuenteIngreso ingreso) {
        Connection conexion = null;
        PreparedStatement statement = null;
        boolean exito = false;

        try {
            conexion = DatabaseConnection.getConnection();
            // Desactiva el auto-commit para manejar transacciones
            conexion.setAutoCommit(false);

            String sql = "INSERT INTO fuentes_ingreso (id_usuario, descripcion, monto, fecha_ingreso, categoria, observaciones) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, ingreso.getIdUsuario());
            statement.setString(2, ingreso.getDescripcion());
            statement.setDouble(3, ingreso.getMonto());
            statement.setDate(4, new java.sql.Date(ingreso.getFechaIngreso().getTime()));
            statement.setString(5, ingreso.getCategoria());
            statement.setString(6, ingreso.getObservaciones());

            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;

            // Confirma la transacción
            conexion.commit();
        } catch (SQLException e) {
            try {
                // Si hay error, rollback
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.err.println("Error al agregar fuente de ingreso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                // Cierra el statement 
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar statement: " + e.getMessage());
            }

        }

        return exito;
    }

    public boolean actualizarIngreso(FuenteIngreso ingreso) {
        Connection conexion = null;
        PreparedStatement statement = null;
        boolean exito = false;

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "UPDATE fuentes_ingreso SET descripcion = ?, monto = ?, fecha_ingreso = ?, "
                    + "categoria = ?, observaciones = ? WHERE id_ingreso = ?";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, ingreso.getDescripcion());
            statement.setDouble(2, ingreso.getMonto());
            statement.setDate(3, new java.sql.Date(ingreso.getFechaIngreso().getTime()));
            statement.setString(4, ingreso.getCategoria());
            statement.setString(5, ingreso.getObservaciones());
            statement.setInt(6, ingreso.getIdIngreso());

            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar fuente de ingreso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return exito;
    }

    public boolean eliminarIngreso(int idIngreso) {
        Connection conexion = null;
        PreparedStatement statement = null;
        boolean exito = false;

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "DELETE FROM fuentes_ingreso WHERE id_ingreso = ?";
            statement = conexion.prepareStatement(sql);
            statement.setInt(1, idIngreso);

            int filasAfectadas = statement.executeUpdate();
            exito = filasAfectadas > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar fuente de ingreso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conexion != null) {
                    DatabaseConnection.closeConnection();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return exito;
    }

    public List<FuenteIngreso> obtenerTodosLosIngresos() {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<FuenteIngreso> ingresos = new ArrayList<>();

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id_ingreso, id_usuario, descripcion, monto, fecha_ingreso, categoria, observaciones "
                    + "FROM fuentes_ingreso";
            statement = conexion.prepareStatement(sql);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                FuenteIngreso ingreso = new FuenteIngreso();
                ingreso.setIdIngreso(resultado.getInt("id_ingreso"));
                ingreso.setIdUsuario(resultado.getInt("id_usuario"));
                ingreso.setDescripcion(resultado.getString("descripcion"));
                ingreso.setMonto(resultado.getDouble("monto"));
                ingreso.setFechaIngreso(resultado.getDate("fecha_ingreso"));
                ingreso.setCategoria(resultado.getString("categoria"));
                ingreso.setObservaciones(resultado.getString("observaciones"));

                ingresos.add(ingreso);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las fuentes de ingreso: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cierra recursos 
        }

        return ingresos;
    }

    public List<FuenteIngreso> buscarPorCategoria(String categoria) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<FuenteIngreso> ingresos = new ArrayList<>();

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id_ingreso, id_usuario, descripcion, monto, fecha_ingreso, categoria, observaciones "
                    + "FROM fuentes_ingreso WHERE categoria = ?";
            statement = conexion.prepareStatement(sql);
            statement.setString(1, categoria);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                FuenteIngreso ingreso = new FuenteIngreso();
                ingreso.setIdIngreso(resultado.getInt("id_ingreso"));
                ingreso.setIdUsuario(resultado.getInt("id_usuario"));
                ingreso.setDescripcion(resultado.getString("descripcion"));
                ingreso.setMonto(resultado.getDouble("monto"));
                ingreso.setFechaIngreso(resultado.getDate("fecha_ingreso"));
                ingreso.setCategoria(resultado.getString("categoria"));
                ingreso.setObservaciones(resultado.getString("observaciones"));

                ingresos.add(ingreso);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por categoría: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Cierra recursos
        }

        return ingresos;
    }

    // jimmmy
    public List<FuenteIngreso> buscarPorMonto(double monto) {
        Connection conexion = null;
        PreparedStatement statement = null;
        ResultSet resultado = null;
        List<FuenteIngreso> ingresos = new ArrayList<>();

        try {
            conexion = DatabaseConnection.getConnection();
            String sql = "SELECT id_ingreso, id_usuario, descripcion, monto, fecha_ingreso, categoria, observaciones "
                    + "FROM fuentes_ingreso WHERE monto = ?";
            statement = conexion.prepareStatement(sql);
            statement.setDouble(1, monto);
            resultado = statement.executeQuery();

            while (resultado.next()) {
                FuenteIngreso ingreso = new FuenteIngreso();
                ingreso.setIdIngreso(resultado.getInt("id_ingreso"));
                ingreso.setIdUsuario(resultado.getInt("id_usuario"));
                ingreso.setDescripcion(resultado.getString("descripcion"));
                ingreso.setMonto(resultado.getDouble("monto"));
                ingreso.setFechaIngreso(resultado.getDate("fecha_ingreso"));
                ingreso.setCategoria(resultado.getString("categoria"));
                ingreso.setObservaciones(resultado.getString("observaciones"));

                ingresos.add(ingreso);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar por monto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // jimmy
        }

        return ingresos;
    }
}
