package com.ahorrofamiliar.service;

import com.ahorrofamiliar.dto.UsuarioIngresoDTO;
import com.ahorrofamiliar.utils.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class for usuario_ingreso table operations
 */
public class UsuarioIngresoService {

    private static final Logger logger = Logger.getLogger(UsuarioIngresoService.class.getName());
    private Connection connection;

    public UsuarioIngresoService() {
        try {
            this.connection = DatabaseConnection.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error connecting to database", e);
            // In a real application, you might want to handle this more gracefully,
            // perhaps by showing a fatal error message and exiting.
        }
    }

    /**
     * Creates a new usuario_ingreso record
     */
    public boolean crearIngreso(UsuarioIngresoDTO ingreso) throws SQLException {
        String sql = "INSERT INTO usuario_ingreso (id_usuario, Aporte, Situacion) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ingreso.getIdUsuario());
            stmt.setBigDecimal(2, ingreso.getAporte());
            stmt.setString(3, ingreso.getSituacion());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating ingreso", e);
            throw e;  // Rethrow the exception after logging it
        }
    }

    /**
     * Retrieves all usuario_ingreso records
     */
    public List<UsuarioIngresoDTO> listarTodosIngresos() throws SQLException {
        String sql = "SELECT id, id_usuario, Aporte, Situacion FROM usuario_ingreso ORDER BY id DESC";
        List<UsuarioIngresoDTO> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UsuarioIngresoDTO dto = new UsuarioIngresoDTO();
                dto.setId(rs.getInt("id"));
                dto.setIdUsuario(rs.getInt("id_usuario"));
                dto.setAporte(rs.getBigDecimal("Aporte"));
                dto.setSituacion(rs.getString("Situacion"));
                lista.add(dto);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error listing ingresos", e);
            throw e;
        }

        return lista;
    }

    /**
     * Searches usuario_ingreso records by user ID
     */
    public List<UsuarioIngresoDTO> buscarIngresosPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT id, id_usuario, Aporte, Situacion FROM usuario_ingreso WHERE id_usuario = ? ORDER BY id DESC";
        List<UsuarioIngresoDTO> lista = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UsuarioIngresoDTO dto = new UsuarioIngresoDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setIdUsuario(rs.getInt("id_usuario"));
                    dto.setAporte(rs.getBigDecimal("Aporte"));
                    dto.setSituacion(rs.getString("Situacion"));
                    lista.add(dto);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching ingresos by usuario", e);
            throw e;
        }

        return lista;
    }

    /**
     * Retrieves a single usuario_ingreso record by its ID.
     * @param id The ID of the ingreso to retrieve.
     * @return UsuarioIngresoDTO if found, null otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public UsuarioIngresoDTO obtenerIngresoPorId(int id) throws SQLException {
        String sql = "SELECT id, id_usuario, Aporte, Situacion FROM usuario_ingreso WHERE id = ?";
        UsuarioIngresoDTO ingreso = null;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ingreso = new UsuarioIngresoDTO();
                    ingreso.setId(rs.getInt("id"));
                    ingreso.setIdUsuario(rs.getInt("id_usuario"));
                    ingreso.setAporte(rs.getBigDecimal("Aporte"));
                    ingreso.setSituacion(rs.getString("Situacion"));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error obtaining ingreso by ID", e);
            throw e;
        }
        return ingreso;
    }

    /**
     * Updates an existing usuario_ingreso record.
     * @param ingreso The UsuarioIngresoDTO object with updated data.
     * @return true if the update was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean actualizarIngreso(UsuarioIngresoDTO ingreso) throws SQLException {
        String sql = "UPDATE usuario_ingreso SET id_usuario = ?, Aporte = ?, Situacion = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ingreso.getIdUsuario());
            stmt.setBigDecimal(2, ingreso.getAporte());
            stmt.setString(3, ingreso.getSituacion());
            stmt.setInt(4, ingreso.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating ingreso", e);
            throw e;
        }
    }

    /**
     * Deletes a usuario_ingreso record by its ID.
     * @param id The ID of the ingreso to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean eliminarIngreso(int id) throws SQLException {
        String sql = "DELETE FROM usuario_ingreso WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting ingreso", e);
            throw e;
        }
    }

    /**
     * Closes the database connection when done
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error closing connection", e);
        }
    }
}