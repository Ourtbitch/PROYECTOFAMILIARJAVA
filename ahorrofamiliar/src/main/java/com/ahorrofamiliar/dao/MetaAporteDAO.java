package com.ahorrofamiliar.dao;

import com.ahorrofamiliar.models.MetaAporte;
import com.ahorrofamiliar.dto.MetaAporteDTO;
import com.ahorrofamiliar.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MetaAporteDAO {

    // Agregar aporte
    public void agregarAporte(MetaAporte aporte) throws SQLException {
        String sql = "INSERT INTO meta_aporte (id_meta, id_usuario, Aporte_Estimado, Aporte_Real, Fecha_Registro, Situacion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, aporte.getIdMeta());
            ps.setInt(2, aporte.getIdUsuario());
            ps.setDouble(3, aporte.getAporteEstimado());
            ps.setDouble(4, aporte.getAporteReal());
            ps.setDate(5, new java.sql.Date(aporte.getFechaRegistro().getTime()));
            ps.setString(6, aporte.getSituacion());
            ps.executeUpdate();
        }
    }

    // Modificar aporte
    public void modificarAporte(MetaAporte aporte) throws SQLException {
        String sql = "UPDATE meta_aporte SET id_meta = ?, id_usuario = ?, Aporte_Estimado = ?, Aporte_Real = ?, Fecha_Registro = ?, Situacion = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, aporte.getIdMeta());
            ps.setInt(2, aporte.getIdUsuario());
            ps.setDouble(3, aporte.getAporteEstimado());
            ps.setDouble(4, aporte.getAporteReal());
            ps.setDate(5, new java.sql.Date(aporte.getFechaRegistro().getTime()));
            ps.setString(6, aporte.getSituacion());
            ps.setInt(7, aporte.getId());
            ps.executeUpdate();
        }
    }

    // Eliminar aporte
    public void eliminarAporte(int id) throws SQLException {
        String sql = "DELETE FROM meta_aporte WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Listar aportes por meta (modelo base)
    public List<MetaAporte> listarAportesPorMeta(int idMeta) throws SQLException {
        List<MetaAporte> lista = new ArrayList<>();
        String sql = "SELECT * FROM meta_aporte WHERE id_meta = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMeta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MetaAporte aporte = new MetaAporte();
                    aporte.setId(rs.getInt("id"));
                    aporte.setIdMeta(rs.getInt("id_meta"));
                    aporte.setIdUsuario(rs.getInt("id_usuario"));
                    aporte.setAporteEstimado(rs.getDouble("Aporte_Estimado"));
                    aporte.setAporteReal(rs.getDouble("Aporte_Real"));
                    aporte.setFechaRegistro(rs.getDate("Fecha_Registro"));
                    aporte.setSituacion(rs.getString("Situacion"));
                    lista.add(aporte);
                }
            }
        }
        return lista;
    }

    // Listar aportes con datos descriptivos por DNI (usa DTO)
    public List<MetaAporteDTO> listarAportesPorDni(String dni) throws SQLException {
        List<MetaAporteDTO> lista = new ArrayList<>();
        String sql = "SELECT ma.id, m.nombre_meta, CONCAT(u.nombre, ' ', u.apellido) AS nombre_usuario, "
                + "ma.Aporte_Estimado, ma.Aporte_Real, ma.Fecha_Registro, ma.Situacion "
                + "FROM meta_aporte ma "
                + "JOIN meta m ON ma.id_meta = m.id "
                + "JOIN usuario u ON ma.id_usuario = u.id "
                + "WHERE u.Numero_doc = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MetaAporteDTO dto = new MetaAporteDTO(
                            rs.getInt("id"),
                            rs.getString("nombre_meta"),
                            rs.getString("nombre_usuario"),
                            rs.getDouble("Aporte_Estimado"),
                            rs.getDouble("Aporte_Real"),
                            rs.getDate("Fecha_Registro"),
                            rs.getString("Situacion")
                    );
                    lista.add(dto);
                }
            }
        }
        return lista;
    }

    // Listar todos los aportes con datos descriptivos (usa DTO)
    public List<MetaAporteDTO> listarTodosAportes() throws SQLException {
        List<MetaAporteDTO> lista = new ArrayList<>();
        String sql = "SELECT ma.id, m.nombre_meta, CONCAT(u.nombre, ' ', u.apellido) AS nombre_usuario, "
                + "ma.Aporte_Estimado, ma.Aporte_Real, ma.Fecha_Registro, ma.Situacion "
                + "FROM meta_aporte ma "
                + "JOIN meta m ON ma.id_meta = m.id "
                + "JOIN usuario u ON ma.id_usuario = u.id";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                MetaAporteDTO dto = new MetaAporteDTO(
                        rs.getInt("id"),
                        rs.getString("nombre_meta"),
                        rs.getString("nombre_usuario"),
                        rs.getDouble("Aporte_Estimado"),
                        rs.getDouble("Aporte_Real"),
                        rs.getDate("Fecha_Registro"),
                        rs.getString("Situacion")
                );
                lista.add(dto);
            }
        }
        return lista;
    }

    /*
     * Verifica si existe un aporte para el mismo usuario, meta y periodo (mes y año).
     * @param idUsuario ID del usuario.
     * @param idMeta ID de la meta.
     * @param fechaRegistro Fecha del aporte.
     * @return true si existe aporte para ese periodo, false si no.
     * @throws SQLException
     */
    public boolean existeAporteParaPeriodo(int idUsuario, int idMeta, Date fechaRegistro) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meta_aporte WHERE id_usuario = ? AND id_meta = ? AND MONTH(Fecha_Registro) = ? AND YEAR(Fecha_Registro) = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaRegistro);
            int mes = cal.get(Calendar.MONTH) + 1; // Enero = 0, por eso +1
            int anio = cal.get(Calendar.YEAR);

            ps.setInt(1, idUsuario);
            ps.setInt(2, idMeta);
            ps.setInt(3, mes);
            ps.setInt(4, anio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existeOtroAporteParaPeriodo(int idUsuario, int idMeta, Date fechaRegistro, int idAporteActual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meta_aporte WHERE id_usuario = ? AND id_meta = ? AND MONTH(Fecha_Registro) = ? AND YEAR(Fecha_Registro) = ? AND id <> ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaRegistro);
            int mes = cal.get(Calendar.MONTH) + 1; // Enero = 0, por eso +1
            int anio = cal.get(Calendar.YEAR);

            ps.setInt(1, idUsuario);
            ps.setInt(2, idMeta);
            ps.setInt(3, mes);
            ps.setInt(4, anio);
            ps.setInt(5, idAporteActual);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public MetaAporte buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM meta_aporte WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MetaAporte aporte = new MetaAporte();
                    aporte.setId(rs.getInt("id"));
                    aporte.setIdMeta(rs.getInt("id_meta")); // nombre correcto de columna
                    aporte.setIdUsuario(rs.getInt("id_usuario"));
                    aporte.setAporteEstimado(rs.getDouble("aporte_estimado"));
                    aporte.setAporteReal(rs.getDouble("aporte_real"));
                    aporte.setFechaRegistro(rs.getDate("fecha_registro"));
                    aporte.setSituacion(rs.getString("situacion"));
                    return aporte;
                } else {
                    return null; // No encontrado
                }
            }
        }

    }

    // Puedes agregar aquí otros métodos que necesites para listar, actualizar o eliminar aportes.
}
