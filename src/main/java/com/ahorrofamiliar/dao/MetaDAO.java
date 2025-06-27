/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.dao;

/**
 *
 * @author pisarana
 */
import com.ahorrofamiliar.models.Meta;
import com.ahorrofamiliar.utils.DatabaseConnection;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetaDAO {

    /*
     * Verifica si existe una meta con el id dado.
     * @param idMeta El ID de la meta a verificar.
     * @return true si existe, false si no.
     * @throws SQLException
     */
    public boolean existeMeta(int idMeta) throws SQLException {
        String sql = "SELECT COUNT(*) FROM meta WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMeta);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /*
     * Agrega un Comentario a la meta.
     * @param idMeta El ID de la meta a verificar.
     * @return false si existe, true si no.
     * @throws SQLException
     */
//    public boolean comentarMeta(int idMeta, String comentario) {
//        String sql = "UPDATE meta SET comentario = ? WHERE id = ?";
//        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
//            ps.setString(1, comentario);
//            ps.setInt(2, idMeta);
//            return ps.executeUpdate() > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    
    /*
     * Actualiza el monto acumulado real (Importe_final_r) en la tabla meta,
     * sumando todos los aportes reales de meta_aporte para esa meta.
     * @param idMeta El ID de la meta a actualizar.
     * @throws SQLException
     */
    public void actualizarMontoAcumulado(int idMeta) throws SQLException {
        String sql = "UPDATE meta SET Importe_final_r = ("
                + "SELECT IFNULL(SUM(aporte_real), 0) FROM meta_aporte WHERE id_meta = ?"
                + ") WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMeta);
            ps.setInt(2, idMeta);
            ps.executeUpdate();
        }
    }

    public List<Meta> listarMetas() throws SQLException {
        List<Meta> lista = new ArrayList<>();
        String sql = "SELECT id, nombre_meta, Importe_final_r FROM meta"; // Ajusta columnas según tu tabla

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Meta meta = new Meta();
                meta.setId(rs.getInt("id"));
                meta.setNombre_meta(rs.getString("nombre_meta")); // coincide con setNombre_meta
                meta.setImporte_Final_r(rs.getDouble("Importe_final_r")); // coincide con setImporte_Final_r
                lista.add(meta);
            }
        }
        return lista;
    }

    public List<Meta> consulta(String nombre, String estado) {

        List<Meta> lista = new ArrayList();
        Meta p = null;
        String sql = null;
        Connection cn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            cn = DatabaseConnection.getConnection();
            //Por jimmy
            if (nombre.equals("") && estado.equals("")) {
                sql = "SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, "
                        + "m.id_categoria, c.nombre as nombre_categoria,m.Comentario "
                        + "FROM meta m LEFT JOIN meta_categoria c ON m.id_categoria = c.id "
                        + "WHERE m.situacion= 'A'";
                //jimmy de meta
                statement = cn.prepareStatement(sql);
                rs = statement.executeQuery();
            } else if (nombre.equals("") && estado != "") {
                sql = "SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, "
                        + "m.id_categoria, c.nombre as nombre_categoria,m.Comentario "
                        + "FROM meta m LEFT JOIN meta_categoria c ON m.id_categoria = c.id "
                        + "WHERE m.situacion= 'A' and m.Estado= ?";
                statement = cn.prepareStatement(sql);
                statement.setString(1, estado);
                rs = statement.executeQuery();
            } else if (estado.equals("") && nombre != "") {
                sql = "SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, "
                        + "m.id_categoria, c.nombre as nombre_categoria,m.Comentario "
                        + "FROM meta m LEFT JOIN meta_categoria c ON m.id_categoria = c.id "
                        + "WHERE m.situacion= 'A' and m.nombre like CONCAT('%', ?, '%')";
                statement = cn.prepareStatement(sql);
                statement.setString(1, nombre);
                rs = statement.executeQuery();
            } else {
                sql = "SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, "
                        + "m.id_categoria, c.nombre as nombre_categoria,m.Comentario "
                        + "FROM meta m LEFT JOIN meta_categoria c ON m.id_categoria = c.id "
                        + "WHERE m.situacion= 'A'and m.nombre_meta like CONCAT('%', ?, '%') and m.Estado= ?";
                statement = cn.prepareStatement(sql);
                statement.setString(1, nombre);
                statement.setString(2, estado);
                rs = statement.executeQuery();
            }

            while (rs.next()) {
                p = new Meta();
                p.setId(rs.getInt(1));
                p.setNombre_meta(rs.getString(2));
                p.setImporte(rs.getDouble(3));
                p.setFecha_creacion(rs.getDate(4));
                p.setFecha_fin(rs.getDate(5));
                p.setEstado(rs.getString(6));
                p.setIdCategoria(rs.getInt(7));
                p.setNombreCategoria(rs.getString(8));
                p.setComentario(rs.getString(9)); //por si acaso
                lista.add(p);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    public List<Meta> consulta_cierre(String estado) {

        List<Meta> lista = new ArrayList();
        Meta p = null;
        String sql = null;
        Connection cn = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            cn = DatabaseConnection.getConnection();
            if (estado.equals("")) {                                                                       //,comentario
                sql = "SELECT id,nombre_meta,Importe_final_e,Importe_final_r,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A'";
                statement = cn.prepareStatement(sql);
                rs = statement.executeQuery();
            } else {                                                                                         //,comentario
                sql = "SELECT id,nombre_meta,Importe_final_e,Importe_final_r,fecha_creacion,Fecha_fin,Estado FROM meta where situacion= 'A' and Estado= ?";
                statement = cn.prepareStatement(sql);
                statement.setString(1, estado);
                rs = statement.executeQuery();
            }

            while (rs.next()) {
                p = new Meta();
                p.setId(rs.getInt(1));
                p.setNombre_meta(rs.getString(2));
                p.setImporte_Final_e(rs.getDouble(3));
                p.setImporte_Final_r(rs.getDouble(4));
                p.setFecha_creacion(rs.getDate(5));
                p.setFecha_fin(rs.getDate(6));
                p.setEstado(rs.getString(7));

                lista.add(p);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lista;
    }

    //metodo adicionar
    public void adicion(Meta p) {
        Connection cn = null;

        try {
            cn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO meta VALUES (null,?,?,?,?,?,?,?,?,?,?,?,?) ";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, p.getNombre_meta());

            st.setInt(2, p.getIdCategoria());

            st.setDouble(3, p.getImporte());

            java.util.Date utilDate = p.getFecha_creacion();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(4, sqlDate);

            utilDate = p.getFecha_fin();
            sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(5, sqlDate);

            st.setString(6, p.getEstado());
            st.setDouble(7, p.getImporte_Inicial_e());
            st.setDouble(8, p.getImporte_Final_e());
            st.setDouble(9, p.getImporte_Inicial_r());
            st.setDouble(10, p.getImporte_Final_r());
            st.setString(11, p.getSituacion());
            st.setString(12, p.getComentario());

            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //metodo modificar
    public void modifica(Meta p) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            String sql = "UPDATE meta SET nombre_meta = ?, Importe = ?, Estado = ?, Fecha_fin = ?, id_categoria = ?, Comentario = ? WHERE id = ?";
            PreparedStatement st = cn.prepareStatement(sql);

            st.setString(1, p.getNombre_meta());
            st.setDouble(2, p.getImporte());
            st.setString(3, p.getEstado());

            java.util.Date utilDate = p.getFecha_fin();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            st.setDate(4, sqlDate);

            st.setInt(5, p.getIdCategoria()); // Aquí está la corrección
            st.setString(6, p.getComentario());
            st.setInt(7, p.getId());

            st.executeUpdate();
            System.out.println("Meta actualizada correctamente.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //metodo anular
    public void anular(int nro, String situacion) {
        Connection cn = null;
        try {
            cn = DatabaseConnection.getConnection();
            String sql = "UPDATE meta SET  Situacion = ? where id = ?";
            PreparedStatement st = cn.prepareStatement(sql);
            st.setString(1, situacion);
            st.setInt(2, nro);
            st.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void CalculaMontosMeta(int id_meta, String anio_mes) {

        String procedimiento = "{ CALL Cerrar_X_META(?, ?) }";

        try (Connection conn = DatabaseConnection.getConnection(); CallableStatement stmt = conn.prepareCall(procedimiento)) {

            // Asignar parámetros
            stmt.setInt(1, id_meta);
            stmt.setString(2, anio_mes);

            // Ejecutar procedimiento
            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String obtenerFechaCierre() throws SQLException 
    {
        Connection conexion = null;
        conexion = DatabaseConnection.getConnection();
        String fecha = null;
        String sql = """
            SELECT IFNULL(
                (SELECT DATE_FORMAT(MAX(fecha_ingreso), '%Y-%m')
                 FROM fuentes_ingreso),
                'SIN FUENTES DE INGRESO'
            ) AS FECHA
            """;

        try (PreparedStatement stmt = conexion.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                fecha = rs.getString("FECHA");
            }

        } catch (SQLException e) {
            e.printStackTrace();  // O usar logging
        }

        return fecha;
    }
    
    


    // Nuevo método para listar metas por categoría por jimmy
    public List<Meta> listarPorCategoria(int idCategoria) throws SQLException {
        List<Meta> lista = new ArrayList<>();
        String sql = "SELECT m.id, m.nombre_meta, m.Importe_final_r, c.nombre as categoria "
                + "FROM meta m LEFT JOIN meta_categoria c ON m.id_categoria = c.id "
                + "WHERE m.situacion = 'A' AND (m.id_categoria = ? OR ? = 0)";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            ps.setInt(2, idCategoria);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Meta meta = new Meta();
                    meta.setId(rs.getInt("id"));
                    meta.setNombre_meta(rs.getString("nombre_meta"));
                    meta.setImporte_Final_r(rs.getDouble("Importe_final_r"));
                    meta.setNombreCategoria(rs.getString("categoria"));
                    lista.add(meta);
                }
            }
        }
        return lista;
    }
    // ======= NUEVOS MÉTODOS AGREGADOS PARA CONSULTAR ESTADO DE META =======

    // Clase interna para representar el estado completo de una meta
    public static class EstadoMeta {

        private int id;
        private String nombreMeta;
        private double importeObjetivo;
        private Date fechaCreacion;
        private Date fechaFin;
        private String estado;
        private String situacion;
        private double totalAportado;
        private double montoFaltante;
        private double porcentajeAvance;
        private int totalAportes;

        // Constructor
        public EstadoMeta(int id, String nombreMeta, double importeObjetivo,
                Date fechaCreacion, Date fechaFin, String estado,
                String situacion, double totalAportado,
                double montoFaltante, double porcentajeAvance, int totalAportes) {
            this.id = id;
            this.nombreMeta = nombreMeta;
            this.importeObjetivo = importeObjetivo;
            this.fechaCreacion = fechaCreacion;
            this.fechaFin = fechaFin;
            this.estado = estado;
            this.situacion = situacion;
            this.totalAportado = totalAportado;
            this.montoFaltante = montoFaltante;
            this.porcentajeAvance = porcentajeAvance;
            this.totalAportes = totalAportes;
        }

        // Getters
        public int getId() {
            return id;
        }

        public String getNombreMeta() {
            return nombreMeta;
        }

        public double getImporteObjetivo() {
            return importeObjetivo;
        }

        public Date getFechaCreacion() {
            return fechaCreacion;
        }

        public Date getFechaFin() {
            return fechaFin;
        }

        public String getEstado() {
            return estado;
        }

        public String getSituacion() {
            return situacion;
        }

        public double getTotalAportado() {
            return totalAportado;
        }

        public double getMontoFaltante() {
            return montoFaltante;
        }

        public double getPorcentajeAvance() {
            return porcentajeAvance;
        }

        public int getTotalAportes() {
            return totalAportes;
        }

        public String getSituacionDescripcion() {
            switch (situacion) {
                case "A":
                    return "Activo";
                case "I":
                    return "Inactivo";
                case "C":
                    return "Cerrado";
                default:
                    return situacion;
            }
        }
    }

    /**
     * Consulta todas las metas con su estado y avance
     */
    public List<EstadoMeta> consultarTodasLasMetasConEstado() throws SQLException {
        return consultarEstadoMetas("", "");
    }

    /**
     * Consulta metas filtradas por nombre y/o situación con información
     * completa del estado
     *
     * @param nombreMeta Nombre de la meta a buscar (puede ser vacío)
     * @param situacion Situación de la meta (A, I, C o vacío para todas)
     * @return Lista de EstadoMeta con información completa
     */
    public List<EstadoMeta> consultarEstadoMetas(String nombreMeta, String situacion) throws SQLException {
        List<EstadoMeta> metas = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, ");
        sql.append("m.Estado, m.Situacion, ");
        sql.append("COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0) as total_aportado, ");
        sql.append("(m.Importe - COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0)) as monto_faltante, ");
        sql.append("ROUND((COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0) / m.Importe) * 100, 2) as porcentaje_avance, ");
        sql.append("COUNT(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.id ELSE NULL END) as total_aportes ");
        sql.append("FROM meta m ");
        sql.append("LEFT JOIN meta_aporte ma ON m.id = ma.id_meta ");
        sql.append("WHERE 1=1 ");

        // Aplicar filtros
        if (nombreMeta != null && !nombreMeta.trim().isEmpty()) {
            sql.append("AND m.nombre_meta LIKE ? ");
        }
        if (situacion != null && !situacion.trim().isEmpty()) {
            sql.append("AND m.Situacion = ? ");
        }

        sql.append("GROUP BY m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, m.Situacion ");
        sql.append("ORDER BY m.fecha_creacion DESC");

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (nombreMeta != null && !nombreMeta.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + nombreMeta.trim() + "%");
            }
            if (situacion != null && !situacion.trim().isEmpty()) {
                pstmt.setString(paramIndex++, situacion.trim());
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EstadoMeta meta = new EstadoMeta(
                            rs.getInt("id"),
                            rs.getString("nombre_meta"),
                            rs.getDouble("Importe"),
                            rs.getDate("fecha_creacion"),
                            rs.getDate("Fecha_fin"),
                            rs.getString("Estado"),
                            rs.getString("Situacion"),
                            rs.getDouble("total_aportado"),
                            rs.getDouble("monto_faltante"),
                            rs.getDouble("porcentaje_avance"),
                            rs.getInt("total_aportes")
                    );
                    metas.add(meta);
                }
            }
        }

        return metas;
    }

    /**
     * Obtiene el detalle completo de una meta específica
     */
    public EstadoMeta obtenerDetalleMetaPorId(int idMeta) throws SQLException {
        String sql = "SELECT m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, "
                + "m.Estado, m.Situacion, "
                + "COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0) as total_aportado, "
                + "(m.Importe - COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0)) as monto_faltante, "
                + "ROUND((COALESCE(SUM(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.aporte_real ELSE 0 END), 0) / m.Importe) * 100, 2) as porcentaje_avance, "
                + "COUNT(CASE WHEN ma.Situacion IN ('A', 'ACT') THEN ma.id ELSE NULL END) as total_aportes "
                + "FROM meta m "
                + "LEFT JOIN meta_aporte ma ON m.id = ma.id_meta "
                + "WHERE m.id = ? "
                + "GROUP BY m.id, m.nombre_meta, m.Importe, m.fecha_creacion, m.Fecha_fin, m.Estado, m.Situacion";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, idMeta);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new EstadoMeta(
                            rs.getInt("id"),
                            rs.getString("nombre_meta"),
                            rs.getDouble("Importe"),
                            rs.getDate("fecha_creacion"),
                            rs.getDate("Fecha_fin"),
                            rs.getString("Estado"),
                            rs.getString("Situacion"),
                            rs.getDouble("total_aportado"),
                            rs.getDouble("monto_faltante"),
                            rs.getDouble("porcentaje_avance"),
                            rs.getInt("total_aportes")
                    );
                }
            }
        }

        return null; // Meta no encontrada
    }

    /**
     * Obtiene estadísticas generales de las metas
     */
    public EstadisticasMetas obtenerEstadisticasGenerales() throws SQLException {
        String sql = "SELECT "
                + "COUNT(*) as total_metas, "
                + "SUM(CASE WHEN Situacion = 'A' THEN 1 ELSE 0 END) as metas_activas, "
                + "SUM(CASE WHEN Situacion = 'C' THEN 1 ELSE 0 END) as metas_cerradas, "
                + "SUM(CASE WHEN Situacion = 'I' THEN 1 ELSE 0 END) as metas_inactivas, "
                + "AVG(Importe) as promedio_importe, "
                + "SUM(Importe) as total_importe_objetivo "
                + "FROM meta";

        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return new EstadisticasMetas(
                        rs.getInt("total_metas"),
                        rs.getInt("metas_activas"),
                        rs.getInt("metas_cerradas"),
                        rs.getInt("metas_inactivas"),
                        rs.getDouble("promedio_importe"),
                        rs.getDouble("total_importe_objetivo")
                );
            }
        }

        return new EstadisticasMetas(0, 0, 0, 0, 0.0, 0.0);
    }

    // Clase interna para estadísticas
    public static class EstadisticasMetas {

        private int totalMetas;
        private int metasActivas;
        private int metasCerradas;
        private int metasInactivas;
        private double promedioImporte;
        private double totalImporteObjetivo;

        public EstadisticasMetas(int totalMetas, int metasActivas, int metasCerradas,
                int metasInactivas, double promedioImporte, double totalImporteObjetivo) {
            this.totalMetas = totalMetas;
            this.metasActivas = metasActivas;
            this.metasCerradas = metasCerradas;
            this.metasInactivas = metasInactivas;
            this.promedioImporte = promedioImporte;
            this.totalImporteObjetivo = totalImporteObjetivo;
        }

        // Getters
        public int getTotalMetas() {
            return totalMetas;
        }

        public int getMetasActivas() {
            return metasActivas;
        }

        public int getMetasCerradas() {
            return metasCerradas;
        }

        public int getMetasInactivas() {
            return metasInactivas;
        }

        public double getPromedioImporte() {
            return promedioImporte;
        }

        public double getTotalImporteObjetivo() {
            return totalImporteObjetivo;
        }
    }

}
