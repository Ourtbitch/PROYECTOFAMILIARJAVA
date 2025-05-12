package com.ahorrofamiliar.service;

import com.ahorrofamiliar.dao.MetaAporteDAO;
import com.ahorrofamiliar.dao.MetaDAO;
import com.ahorrofamiliar.dao.UsuarioDAO;
import com.ahorrofamiliar.dto.MetaAporteDTO;
import com.ahorrofamiliar.models.MetaAporte;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MetaAporteService {

    private MetaAporteDAO metaAporteDAO;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private MetaDAO metaDAO = new MetaDAO();
    // Lista con los valores válidos para el campo 'situacion'
    private static final List<String> SITUACIONES_VALIDAS = Arrays.asList("ACT", "INA");

    public MetaAporteService() {
        this.metaAporteDAO = new MetaAporteDAO();
    }

    public void insertarAporte(MetaAporte aporte) throws Exception {
        // 1. Validar que el usuario existe
        if (!usuarioDAO.existeUsuario(aporte.getIdUsuario())) {
            throw new Exception("El usuario con ID " + aporte.getIdUsuario() + " no existe.");
        }

        // 2. Validar que la meta existe
        if (!metaDAO.existeMeta(aporte.getIdMeta())) {
            throw new Exception("La meta con ID " + aporte.getIdMeta() + " no existe.");
        }

        // 3. Validar que no exista aporte duplicado para mismo usuario/meta/mes-año
        java.sql.Date sqlDate = new java.sql.Date(aporte.getFechaRegistro().getTime());
        if (metaAporteDAO.existeAporteParaPeriodo(aporte.getIdUsuario(), aporte.getIdMeta(), sqlDate)) {
            throw new Exception("Ya existe un aporte para este usuario, meta y periodo.");
        }

        // 4. Insertar aporte
        metaAporteDAO.agregarAporte(aporte);

        // 5. (Opcional) Actualizar resumen en tabla meta
        metaDAO.actualizarMontoAcumulado(aporte.getIdMeta());
    }

    /*
      Agrega un nuevo aporte después de validar los datos y verificar que no exista un aporte duplicado
     */
    public void agregarAporte(MetaAporte aporte) throws SQLException, Exception {
        validarAporte(aporte); // Validaciones básicas y específicas

        // Verifica que no exista un aporte para el mismo usuario, meta y mes/año
        java.sql.Date sqlDate = new java.sql.Date(aporte.getFechaRegistro().getTime());
        if (metaAporteDAO.existeAporteParaPeriodo(aporte.getIdUsuario(), aporte.getIdMeta(), sqlDate)) {
            throw new Exception("Ya existe un aporte para este usuario, meta y periodo.");
        }

        // Si todo está bien, llama al DAO para insertar el aporte en la BD
        metaAporteDAO.agregarAporte(aporte);
    }

    /*
     * Modifica un aporte existente después de validar y verificar que no se duplique
     */
    public void modificarAporte(MetaAporte aporte) throws SQLException, Exception {
        // Verifica que el id sea válido (mayor que cero)
        if (aporte.getId() <= 0) {
            throw new IllegalArgumentException("ID de aporte inválido.");
        }

        // Si todo está bien, llama al DAO para actualizar el aporte en la BD
        metaAporteDAO.modificarAporte(aporte);
    }

    /*
     * Elimina un aporte por id, validando que el id sea válido
     */
    public void eliminarAporte(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de aporte inválido.");
        }
        metaAporteDAO.eliminarAporte(id);
    }

    /*
     * Lista todos los aportes asociados a una meta específica, validando el id de la meta
     */
    public List<MetaAporte> listarAportesPorMeta(int idMeta) throws SQLException {
        if (idMeta <= 0) {
            throw new IllegalArgumentException("ID de meta inválido.");
        }
        return metaAporteDAO.listarAportesPorMeta(idMeta);
    }

    /*
     * Valida los campos del objeto MetaAporte para asegurarse que tengan valores correctos y lógicos
     */
    private void validarAporte(MetaAporte aporte) throws SQLException {
        if (aporte.getIdMeta() <= 0) {
            throw new IllegalArgumentException("ID de meta inválido.");
        }
        if (aporte.getIdUsuario() <= 0) {
            throw new IllegalArgumentException("ID de usuario inválido.");
        }
        if (aporte.getAporteEstimado() < 0) {
            throw new IllegalArgumentException("El aporte estimado no puede ser negativo.");
        }
        if (aporte.getAporteReal() < 0) {
            throw new IllegalArgumentException("El aporte real no puede ser negativo.");
        }
        if (aporte.getAporteReal() > aporte.getAporteEstimado()) {
            throw new IllegalArgumentException("El aporte real no puede ser mayor que el estimado.");
        }
        if (aporte.getFechaRegistro() == null) {
            throw new IllegalArgumentException("La fecha de registro es obligatoria.");
        }
        if (aporte.getFechaRegistro().after(new Date())) {
            throw new IllegalArgumentException("La fecha de registro no puede ser futura.");
        }
        if (aporte.getSituacion() == null || aporte.getSituacion().isEmpty()) {
            throw new IllegalArgumentException("La situación es obligatoria.");
        }
        if (!SITUACIONES_VALIDAS.contains(aporte.getSituacion())) {
            throw new IllegalArgumentException("Situación inválida. Valores permitidos: " + SITUACIONES_VALIDAS);
        }
        java.sql.Date sqlDate = new java.sql.Date(aporte.getFechaRegistro().getTime());
        
        // Validar duplicados SOLO si es un nuevo aporte (insert)
        if (aporte.getId() <= 0) {
            if (metaAporteDAO.existeAporteParaPeriodo(
                    aporte.getIdUsuario(),
                    aporte.getIdMeta(),
                    sqlDate)) {
                throw new IllegalArgumentException("Ya existe un aporte para este usuario, meta y periodo.");
            }
        }

    }

    /*
     * Verifica si ya existe un aporte para el mismo usuario, meta y mes/año
     */
    private boolean existeAporte(int idUsuario, int idMeta, Date fechaRegistro) throws SQLException {
        List<MetaAporte> aportes = metaAporteDAO.listarAportesPorMeta(idMeta);

        // Busca en la lista si hay un aporte con el mismo usuario y mismo mes/año
        return aportes.stream().anyMatch(a
                -> a.getIdUsuario() == idUsuario
                && mismoMesAnio(a.getFechaRegistro(), fechaRegistro)
        );
    }

    /*
     * Verifica si existe otro aporte diferente (por id) con el mismo usuario, meta y mes/año
     */
    private boolean existeAporteDiferenteId(int idUsuario, int idMeta, Date fechaRegistro, int idActual) throws SQLException {
        List<MetaAporte> aportes = metaAporteDAO.listarAportesPorMeta(idMeta);

        // Busca en la lista si hay un aporte distinto al actual con mismo usuario y mismo mes/año
        return aportes.stream().anyMatch(a
                -> a.getIdUsuario() == idUsuario
                && a.getId() != idActual
                && mismoMesAnio(a.getFechaRegistro(), fechaRegistro)
        );
    }

    /*
     * Compara si dos fechas tienen el mismo mes y año (ignora día y hora)
     */
    private boolean mismoMesAnio(Date fecha1, Date fecha2) {
        @SuppressWarnings("deprecation")
        boolean mismoMes = fecha1.getMonth() == fecha2.getMonth();
        @SuppressWarnings("deprecation")
        boolean mismoAnio = fecha1.getYear() == fecha2.getYear();
        return mismoMes && mismoAnio;
    }

    public List<MetaAporteDTO> buscarAportesPorDni(String dni) throws SQLException {
        return metaAporteDAO.listarAportesPorDni(dni);
    }

    public List<MetaAporteDTO> listarTodosAportes() throws SQLException {
        return metaAporteDAO.listarTodosAportes();
    }

    public MetaAporte buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido.");
        }
        return metaAporteDAO.buscarPorId(id);
    }

}
