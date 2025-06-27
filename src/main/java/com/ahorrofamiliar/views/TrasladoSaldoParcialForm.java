/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ahorrofamiliar.views;

import com.ahorrofamiliar.dao.MetaDAO;
import com.ahorrofamiliar.dao.MetaTrasladoDAO;
import com.ahorrofamiliar.models.Meta;
import com.ahorrofamiliar.models.MetaTraslado;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class TrasladoSaldoParcialForm extends javax.swing.JFrame {
    private MetaTrasladoDAO trasladoDAO;
    private MetaDAO metaDAO;
    private DefaultTableModel modeloTabla;
    /**
     * Creates new form TrasladoSaldoForm
     */
    public TrasladoSaldoParcialForm() {
        initComponents();
        trasladoDAO = new MetaTrasladoDAO();
        metaDAO = new MetaDAO();
        configurarTabla(); // Aplica el modelo a la JTable ya existente
        cargarMetas();     // Llena los ComboBox
        cargarHistorial(); // Llena la tabla
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        getContentPane().setBackground(new Color(144, 238, 144)); // Verde claro
        // Fondo de tabla y renderizado
        tblHistorial.setRowHeight(25);
        tblHistorial.setShowGrid(true);
        tblHistorial.setGridColor(Color.LIGHT_GRAY);
        // Encabezado personalizado
        tblHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblHistorial.getTableHeader().setBackground(new Color(205, 171, 45)); // Amarillo mostaza
        tblHistorial.getTableHeader().setForeground(Color.WHITE);

        // Alternancia de filas y selección resaltada
        tblHistorial.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(240, 255, 255) : Color.WHITE); // alternancia
                } else {
                    c.setBackground(new Color(102, 204, 255)); // seleccionado
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        // Aplicar estilos a botones
        estilizarBoton(btnRealizarTraslado, new Color(135, 230, 145));
        estilizarBoton(btnCancelar, new Color(255, 102, 102));
        estilizarBoton(btnCerrar, new Color(255, 102, 102));

        // Estilo a campos 
        txtMonto.setBackground(Color.WHITE);
        txtSaldoDisponible.setBackground(Color.WHITE);
    }

    private void estilizarBoton(javax.swing.JButton boton, Color fondo) {
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Meta Origen");
        modeloTabla.addColumn("Meta Destino");
        modeloTabla.addColumn("Monto");
        tblHistorial.setModel(modeloTabla);
    }

    private void cargarMetas() {
        try {
            List<Meta> metas = metaDAO.listarMetas();
            DefaultComboBoxModel<Meta> modeloOrigen = new DefaultComboBoxModel<>();
            DefaultComboBoxModel<Meta> modeloDestino = new DefaultComboBoxModel<>();

            for (Meta meta : metas) {
                modeloOrigen.addElement(meta);
                modeloDestino.addElement(meta);
            }

            cbMetaOrigen.setModel(modeloOrigen);
            cbMetaDestino.setModel(modeloDestino);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar metas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        try {
            List<MetaTraslado> traslados = trasladoDAO.listarHistorialTraslados();
            modeloTabla.setRowCount(0); // Limpiar tabla

            for (MetaTraslado traslado : traslados) {
                Object[] fila = {
                    traslado.getId(),
                    traslado.getFechaTraslado(),
                    traslado.getNombreMetaOrigen(),
                    traslado.getNombreMetaDestino(),
                    traslado.getMonto()
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtMonto = new javax.swing.JTextField();
        btnRealizarTraslado = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtSaldoDisponible = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbMetaOrigen = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cbMetaDestino = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtMonto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMontoActionPerformed(evt);
            }
        });

        btnRealizarTraslado.setText("Realizar Traslado");
        btnRealizarTraslado.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRealizarTraslado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRealizarTrasladoActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        txtSaldoDisponible.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSaldoDisponibleActionPerformed(evt);
            }
        });

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Meta Origen", "Saldo Disponible", "Meta Destino", "Monto Total"
            }
        ));
        tblHistorial.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(tblHistorial);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Traslado de Saldos Parcial");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnCerrar.setText("X");
        btnCerrar.setBorder(null);
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        jLabel2.setText("Meta Origen");

        cbMetaOrigen.setModel(new javax.swing.DefaultComboBoxModel<>());
        cbMetaOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMetaOrigenActionPerformed(evt);
            }
        });

        jLabel3.setText("Saldo Disponible");

        jLabel5.setText("Meta Destino");

        cbMetaDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMetaDestinoActionPerformed(evt);
            }
        });

        jLabel6.setText("     Monto ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(58, 58, 58)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel5)
                                .addComponent(jLabel6)
                                .addComponent(jLabel3))
                            .addGap(32, 32, 32)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtSaldoDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbMetaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbMetaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(95, 95, 95)
                            .addComponent(btnRealizarTraslado)
                            .addGap(94, 94, 94)
                            .addComponent(btnCancelar)))
                    .addGap(296, 296, 296))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(205, 205, 205)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(388, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(btnCerrar))
                    .addGap(39, 39, 39)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(cbMetaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(txtSaldoDisponible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(60, 60, 60)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(cbMetaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(37, 37, 37)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(txtMonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(57, 57, 57)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnRealizarTraslado)
                        .addComponent(btnCancelar))
                    .addContainerGap(328, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMontoActionPerformed
        // TODO add your handling code here:
        txtMonto.setEditable(true);
    }//GEN-LAST:event_txtMontoActionPerformed

    private void btnRealizarTrasladoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRealizarTrasladoActionPerformed
        // TODO add your handling code here
        try {
            Meta metaOrigen = (Meta) cbMetaOrigen.getSelectedItem();
            Meta metaDestino = (Meta) cbMetaDestino.getSelectedItem();

            if (metaOrigen == null || metaDestino == null) {
                JOptionPane.showMessageDialog(this, "Seleccione ambas metas",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (metaOrigen.getId() == metaDestino.getId()) {
                JOptionPane.showMessageDialog(this, "No puede seleccionar la misma meta como origen y destino",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String montoStr = txtMonto.getText().trim();
            if (montoStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un monto válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            montoStr = montoStr.replace(",", ".");  // ✅ reemplazar coma si existe
            double monto = Double.parseDouble(montoStr);
            double saldoDisponible = Double.parseDouble(txtSaldoDisponible.getText().trim().replace(",", "."));

            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto debe ser mayor que cero",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (monto == saldoDisponible) {
                JOptionPane.showMessageDialog(this, "El monto iguala el saldo disponible",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (monto > saldoDisponible) {
                JOptionPane.showMessageDialog(this, "El monto excede el saldo disponible",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idUsuario = 1; // temporal

            MetaTraslado traslado = new MetaTraslado();
            traslado.setIdMetaOrigen(metaOrigen.getId());
            traslado.setIdMetaDestino(metaDestino.getId());
            traslado.setMonto(monto);
            traslado.setFechaTraslado(new Date());
            traslado.setIdUsuario(idUsuario);

            trasladoDAO.realizarTraslado(traslado);

            JOptionPane.showMessageDialog(this, "Traslado realizado con éxito");
            cargarHistorial();
            cbMetaOrigenActionPerformed(null); // refrescar saldo origen
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un monto válido (solo números, sin letras)",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al realizar traslado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
     
    }//GEN-LAST:event_btnRealizarTrasladoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtSaldoDisponibleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSaldoDisponibleActionPerformed
        // TODO add your handling code here:
         try {
            Meta metaSeleccionada = (Meta) cbMetaOrigen.getSelectedItem();
            if (metaSeleccionada != null) {
                double saldo = trasladoDAO.obtenerSaldoDisponible(metaSeleccionada.getId());
                //txtSaldoDisponible.setText(String.format("%.2f", saldo));
                //txtMonto.setText(String.format("%.2f", saldo));  // ✅ AUTO-FILL al campo Monto
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener saldo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txtSaldoDisponibleActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void cbMetaOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMetaOrigenActionPerformed
        // TODO add your handling code here:
        try {
            Meta metaSeleccionada = (Meta) cbMetaOrigen.getSelectedItem();
            if (metaSeleccionada != null) {
                double saldo = trasladoDAO.obtenerSaldoDisponible(metaSeleccionada.getId());
                txtSaldoDisponible.setText(String.format("%.2f", saldo));
                txtMonto.setText(String.format("%.2f", saldo));  // ✅ AUTO-FILL al campo Monto
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener saldo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_cbMetaOrigenActionPerformed

    private void cbMetaDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMetaDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMetaDestinoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new TrasladoSaldoParcialForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnRealizarTraslado;
    private javax.swing.JComboBox<Meta> cbMetaDestino;
    private javax.swing.JComboBox<Meta> cbMetaOrigen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JTextField txtMonto;
    private javax.swing.JTextField txtSaldoDisponible;
    // End of variables declaration//GEN-END:variables
}
