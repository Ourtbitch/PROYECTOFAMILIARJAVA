package com.ahorrofamiliar.views;

import com.ahorrofamiliar.dao.MetaDAO;
import com.ahorrofamiliar.dao.MetaDAO.EstadoMeta;
import com.ahorrofamiliar.dao.MetaDAO.EstadisticasMetas;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/*
    MISAEL CHALLCO REQUERIMIENTO 04 
    
    FECHA   DESCRIPCION
    11/06   CREADO
*/
public class ConsultarEstadoMeta extends JFrame {
    private JTextField txtBuscarMeta;
    private JComboBox<String> cmbSituacion;
    private JButton btnBuscar, btnMostrarTodas, btnLimpiar, btnDetalle, btnEstadisticas;
    private JTable tblMetas;
    private DefaultTableModel modeloTabla;
    private MetaDAO metaDAO;
    private NumberFormat formatoMoneda;
    private SimpleDateFormat formatoFecha;

    public ConsultarEstadoMeta() {
        this.metaDAO = new MetaDAO();
        this.formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
        this.formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        initComponents();
        setupTable();
        cargarTodasLasMetas();
    }

    private void initComponents() {
        setTitle("Consultar Estado de Meta - REQMS-004");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel superior - Filtros
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));
        
        panelFiltros.add(new JLabel("Nombre Meta:"));
        txtBuscarMeta = new JTextField(20);
        panelFiltros.add(txtBuscarMeta);
        
        panelFiltros.add(new JLabel("Situación:"));
        cmbSituacion = new JComboBox<>(new String[]{
            "Todas", 
            "A - Activo", 
            "I - Inactivo", 
            "C - Cerrado"
        });
        panelFiltros.add(cmbSituacion);
        
        btnBuscar = new JButton("Buscar");
        btnMostrarTodas = new JButton("Mostrar Todas");
        btnLimpiar = new JButton("Limpiar");
        btnDetalle = new JButton("Ver Detalle");
        btnEstadisticas = new JButton("Estadísticas");
        
        panelFiltros.add(btnBuscar);
        panelFiltros.add(btnMostrarTodas);
        panelFiltros.add(btnLimpiar);
        panelFiltros.add(btnDetalle);
        panelFiltros.add(btnEstadisticas);
        
        add(panelFiltros, BorderLayout.NORTH);
        
        // Panel central - Tabla
        tblMetas = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblMetas);
        scrollPane.setPreferredSize(new Dimension(1200, 500));
        add(scrollPane, BorderLayout.CENTER);
        
        // Panel inferior - Información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelInfo.add(new JLabel("Nota: Los montos están en Soles (PEN) - Doble clic en una fila para ver detalles"));
        add(panelInfo, BorderLayout.SOUTH);
        
        // Eventos
        btnBuscar.addActionListener(e -> buscarMetas());
        btnMostrarTodas.addActionListener(e -> cargarTodasLasMetas());
        btnLimpiar.addActionListener(e -> limpiarFiltros());
        btnDetalle.addActionListener(e -> mostrarDetalleMetaSeleccionada());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        
        // Enter en campo de búsqueda
        txtBuscarMeta.addActionListener(e -> buscarMetas());
        
        // Doble clic en tabla
        tblMetas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    mostrarDetalleMetaSeleccionada();
                }
            }
        });
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void setupTable() {
        String[] columnas = {
            "ID", "Nombre Meta", "Importe Objetivo", "Total Aportado", 
            "Monto Faltante", "% Avance", "# Aportes", "Fecha Creación", 
            "Fecha Fin", "Estado", "Situación"
        };
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Solo lectura
            }
        };
        
        tblMetas.setModel(modeloTabla);
        
        // Configurar ancho de columnas
        tblMetas.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tblMetas.getColumnModel().getColumn(1).setPreferredWidth(200);  // Nombre
        tblMetas.getColumnModel().getColumn(2).setPreferredWidth(120);  // Importe Objetivo
        tblMetas.getColumnModel().getColumn(3).setPreferredWidth(120);  // Total Aportado
        tblMetas.getColumnModel().getColumn(4).setPreferredWidth(120);  // Faltante
        tblMetas.getColumnModel().getColumn(5).setPreferredWidth(80);   // % Avance
        tblMetas.getColumnModel().getColumn(6).setPreferredWidth(80);   // # Aportes
        tblMetas.getColumnModel().getColumn(7).setPreferredWidth(100);  // Fecha Creación
        tblMetas.getColumnModel().getColumn(8).setPreferredWidth(100);  // Fecha Fin
        tblMetas.getColumnModel().getColumn(9).setPreferredWidth(100);  // Estado
        tblMetas.getColumnModel().getColumn(10).setPreferredWidth(80);  // Situación
        
        tblMetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    private void buscarMetas() {
        String nombreMeta = txtBuscarMeta.getText().trim();
        String situacionSeleccionada = (String) cmbSituacion.getSelectedItem();
        String situacion = "";
        
        if (!situacionSeleccionada.equals("Todas")) {
            situacion = situacionSeleccionada.substring(0, 1); // Obtener solo la letra (A, I, C)
        }
        
        cargarMetas(nombreMeta, situacion);
    }
    
    private void cargarTodasLasMetas() {
        cargarMetas("", "");
    }
    
    private void cargarMetas(String nombreMeta, String situacion) {
        try {
            // Limpiar tabla
            modeloTabla.setRowCount(0);
            
            // Usar el método del DAO
            List<EstadoMeta> metas = metaDAO.consultarEstadoMetas(nombreMeta, situacion);
            
            // Llenar la tabla
            for (EstadoMeta meta : metas) {
                Object[] fila = new Object[11];
                fila[0] = meta.getId();
                fila[1] = meta.getNombreMeta();
                fila[2] = formatoMoneda.format(meta.getImporteObjetivo());
                fila[3] = formatoMoneda.format(meta.getTotalAportado());
                fila[4] = formatoMoneda.format(meta.getMontoFaltante());
                fila[5] = String.format("%.2f%%", meta.getPorcentajeAvance());
                fila[6] = meta.getTotalAportes();
                fila[7] = meta.getFechaCreacion() != null ? formatoFecha.format(meta.getFechaCreacion()) : "";
                fila[8] = meta.getFechaFin() != null ? formatoFecha.format(meta.getFechaFin()) : "";
                fila[9] = meta.getEstado();
                fila[10] = meta.getSituacionDescripcion();
                
                modeloTabla.addRow(fila);
            }
            
            // Mostrar mensaje si no hay resultados
            if (metas.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron metas con los criterios especificados.", 
                    "Sin resultados", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Mostrar resumen en la barra de estado
                setTitle("Consultar Estado de Meta - REQMS-004 (" + metas.size() + " metas encontradas)");
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al consultar las metas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void limpiarFiltros() {
        txtBuscarMeta.setText("");
        cmbSituacion.setSelectedIndex(0);
        setTitle("Consultar Estado de Meta - REQMS-004");
        cargarTodasLasMetas();
    }
    
    public void mostrarDetalleMetaSeleccionada() {
        int filaSeleccionada = tblMetas.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int idMeta = (Integer) modeloTabla.getValueAt(filaSeleccionada, 0);
            mostrarDetalleMeta(idMeta);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una meta para ver los detalles.", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void mostrarDetalleMeta(int idMeta) {
        try {
            EstadoMeta meta = metaDAO.obtenerDetalleMetaPorId(idMeta);
            
            if (meta != null) {
                StringBuilder detalle = new StringBuilder();
                detalle.append("=== DETALLE COMPLETO DE LA META ===\n\n");
                detalle.append("ID: ").append(meta.getId()).append("\n");
                detalle.append("Nombre: ").append(meta.getNombreMeta()).append("\n");
                detalle.append("Estado: ").append(meta.getEstado()).append("\n");
                detalle.append("Situación: ").append(meta.getSituacionDescripcion()).append("\n\n");
                
                detalle.append("=== FECHAS ===\n");
                detalle.append("Fecha de Creación: ").append(
                    meta.getFechaCreacion() != null ? formatoFecha.format(meta.getFechaCreacion()) : "No definida"
                ).append("\n");
                detalle.append("Fecha Límite: ").append(
                    meta.getFechaFin() != null ? formatoFecha.format(meta.getFechaFin()) : "No definida"
                ).append("\n\n");
                
                detalle.append("=== INFORMACIÓN FINANCIERA ===\n");
                detalle.append("Importe Objetivo: ").append(formatoMoneda.format(meta.getImporteObjetivo())).append("\n");
                detalle.append("Total Aportado: ").append(formatoMoneda.format(meta.getTotalAportado())).append("\n");
                detalle.append("Monto Faltante: ").append(formatoMoneda.format(meta.getMontoFaltante())).append("\n");
                detalle.append("Porcentaje de Avance: ").append(String.format("%.2f%%", meta.getPorcentajeAvance())).append("\n");
                detalle.append("Número de Aportes: ").append(meta.getTotalAportes()).append("\n\n");
                
                // Determinar el estado del progreso
                detalle.append("=== ANÁLISIS DEL PROGRESO ===\n");
                if (meta.getPorcentajeAvance() >= 100) {
                    detalle.append("✅ META COMPLETADA - Se ha alcanzado el objetivo\n");
                } else if (meta.getPorcentajeAvance() >= 75) {
                    detalle.append("🟡 META CERCA DEL OBJETIVO - Falta poco para completar\n");
                } else if (meta.getPorcentajeAvance() >= 50) {
                    detalle.append("🟠 META EN PROGRESO - Se ha avanzado la mitad\n");
                } else if (meta.getPorcentajeAvance() > 0) {
                    detalle.append("🔴 META EN ETAPA INICIAL - Aún queda mucho por avanzar\n");
                } else {
                    detalle.append("⚪ META SIN APORTES - No se han registrado aportes aún\n");
                }
                
                JTextArea textArea = new JTextArea(detalle.toString());
                textArea.setEditable(false);
                textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
                
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(this, 
                    scrollPane, 
                    "Detalle de Meta: " + meta.getNombreMeta(), 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No se pudo encontrar la información de la meta seleccionada.", 
                    "Meta no encontrada", 
                    JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al obtener el detalle de la meta: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void mostrarEstadisticas() {
        try {
            EstadisticasMetas estadisticas = metaDAO.obtenerEstadisticasGenerales();
            
            StringBuilder resumen = new StringBuilder();
            resumen.append("=== ESTADÍSTICAS GENERALES DE METAS ===\n\n");
            resumen.append("Total de Metas: ").append(estadisticas.getTotalMetas()).append("\n");
            resumen.append("Metas Activas: ").append(estadisticas.getMetasActivas()).append("\n");
            resumen.append("Metas Cerradas: ").append(estadisticas.getMetasCerradas()).append("\n");
            resumen.append("Metas Inactivas: ").append(estadisticas.getMetasInactivas()).append("\n\n");
            
            resumen.append("=== INFORMACIÓN FINANCIERA ===\n");
            resumen.append("Promedio de Importe por Meta: ").append(formatoMoneda.format(estadisticas.getPromedioImporte())).append("\n");
            resumen.append("Total de Importes Objetivo: ").append(formatoMoneda.format(estadisticas.getTotalImporteObjetivo())).append("\n");
            
            JTextArea textArea = new JTextArea(resumen.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));
            
            JOptionPane.showMessageDialog(this, 
                scrollPane, 
                "Estadísticas del Sistema de Metas", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al obtener las estadísticas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    
}