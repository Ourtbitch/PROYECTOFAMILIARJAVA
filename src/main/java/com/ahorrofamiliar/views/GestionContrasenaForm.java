/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ahorrofamiliar.views;

import com.ahorrofamiliar.dao.GestionContrasenaDAO;
import com.ahorrofamiliar.dao.UsuarioDAO;
import com.ahorrofamiliar.models.GestionContrasena;
import com.ahorrofamiliar.utils.DatabaseConnection;
import com.ahorrofamiliar.utils.PasswordUtils;
import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER
 */
public class GestionContrasenaForm extends javax.swing.JFrame {

    private int idUsuarioActual; // Variable para almacenar el ID del usuario

    // Formato de fecha para mostrar en la tabla
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public GestionContrasenaForm() {
        initComponents();
        initTable();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(255, 255, 153));
    }

    public GestionContrasenaForm(int idUsuario) {
        this();
        this.idUsuarioActual = idUsuario;
        txtIdUsuario.setText(String.valueOf(idUsuario));
        txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        cargarDatosUsuario(idUsuario);
    }

    private void initTable() {
        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Gestión", "ID Usuario", "Hash", "Fecha", "Tipo", "Realizado por"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.Integer.class,
                java.lang.String.class, java.lang.String.class,
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        // Ajustar el ancho de columnas
        tblHistorial.getColumnModel().getColumn(2).setMinWidth(100); // Hash
        tblHistorial.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha
    }

    private void cargarDatosUsuario(int idUsuario) {
        DefaultTableModel model = (DefaultTableModel) tblHistorial.getModel();
        model.setRowCount(0); // Limpiar tabla

        try {
            GestionContrasenaDAO dao = new GestionContrasenaDAO();
            List<GestionContrasena> historial = dao.obtenerHistorialContrasena(idUsuario);

            for (GestionContrasena gestion : historial) {
                // Mostrar solo los últimos 4 caracteres del hash por seguridad
                String hashReducido = gestion.getContrasenaHash().length() > 4
                        ? "****" + gestion.getContrasenaHash().substring(gestion.getContrasenaHash().length() - 4)
                        : gestion.getContrasenaHash();

                model.addRow(new Object[]{
                    gestion.getIdGestion(),
                    gestion.getIdUsuario(),
                    hashReducido,
                    dateFormat.format(gestion.getFechaCambio()),
                    gestion.getTipoCambio(),
                    gestion.getRealizadoPor()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean validarFortalezaContrasena(String contrasena) {
        // Mínimo 8 caracteres, al menos una mayúscula, una minúscula y un número
        String patron = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
        return contrasena.matches(patron);
    }

    private void guardarCambioContrasena() {
        // Validar campos obligatorios
        if (txtHash.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "El campo Hash de contraseña es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nuevaContrasena = new String(txtHash.getPassword());
        String confirmacion = new String(txtConfirmacionHash.getPassword());

        if (!validarFortalezaContrasena(nuevaContrasena)) {
            JOptionPane.showMessageDialog(this,
                    "La contraseña debe tener al menos 8 caracteres, incluyendo mayúsculas, minúsculas y números",
                    "Contraseña débil", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txtRealizadoPor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo 'Realizado por' es obligatorio",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!nuevaContrasena.equals(confirmacion)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conexion = null;
        try {
            int idUsuario = Integer.parseInt(txtIdUsuario.getText());
            String tipoCambio = (String) jComboBox1.getSelectedItem();
            String realizadoPor = txtRealizadoPor.getText();

            // Hashear la contraseña antes de guardarla
            String nuevaContrasenaHash = PasswordUtils.hashPassword(nuevaContrasena);

            // 1. Actualizar contraseña en tabla usuario
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean actualizacionExitosa = usuarioDAO.actualizarContrasena(idUsuario, nuevaContrasenaHash);

            if (actualizacionExitosa) {
                // 2. Registrar el cambio en el historial
                GestionContrasenaDAO gestionDAO = new GestionContrasenaDAO();
                GestionContrasena gestion = new GestionContrasena();
                gestion.setIdUsuario(idUsuario);
                gestion.setContrasenaHash(nuevaContrasenaHash);
                gestion.setFechaCambio(new Date());
                gestion.setTipoCambio(tipoCambio);
                gestion.setRealizadoPor(realizadoPor);

                boolean registroExitoso = gestionDAO.registrarCambioContrasena(gestion);

                if (registroExitoso) {
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada e historial registrado");
                    cargarDatosUsuario(idUsuario); // Actualizar la tabla de historial
                    limpiarCampos();
                    // Establecer la nueva fecha después de limpiar
                    txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada pero falló registro en historial");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID de usuario inválido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    private void limpiarCampos() {
        txtHash.setText("");
        txtConfirmacionHash.setText("");
        txtRealizadoPor.setText("");
        jComboBox1.setSelectedIndex(0);
        // La fewcha no se limpia
    }

    private void filtrarHistorial() {
        if (!txtIdUsuario.getText().isEmpty()) {
            try {
                int idUsuario = Integer.parseInt(txtIdUsuario.getText());
                cargarDatosUsuario(idUsuario);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID Usuario debe ser un número válido",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHistorial = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtIdUsuario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        txtRealizadoPor = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnFiltrar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        txtHash = new javax.swing.JPasswordField();
        txtConfirmacionHash = new javax.swing.JPasswordField();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("GESTION DE CONTRASEÑA DE USUARIO");

        jLabel2.setText("Historial");

        tblHistorial.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Usuario", "Hash", "Fecha", "Tipo", "Realizado por"
            }
        ));
        jScrollPane1.setViewportView(tblHistorial);

        jLabel4.setText("ID Usuario");

        jLabel5.setText("Contraseña Hash");

        jLabel6.setText("Fecha");

        jLabel7.setText("Tipo de Cambio");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cambio Manual", "Recuperacion", "Admin", "Renovacion" }));

        jLabel8.setText("Realizado Por:");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnFiltrar.setText("Filtrar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel3.setText("Repita Contraseña");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(247, 247, 247)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(76, 76, 76)
                                    .addComponent(btnGuardar)
                                    .addGap(108, 108, 108)
                                    .addComponent(btnFiltrar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                                    .addComponent(btnSalir)
                                    .addGap(106, 106, 106))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel3))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtRealizadoPor)
                                        .addComponent(txtFecha)
                                        .addComponent(txtIdUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE)
                                        .addComponent(txtHash)
                                        .addComponent(txtConfirmacionHash))
                                    .addGap(241, 241, 241)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtIdUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(6, 6, 6))
                    .addComponent(txtHash, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtConfirmacionHash, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(txtRealizadoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnFiltrar)
                    .addComponent(btnSalir))
                .addContainerGap(215, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        guardarCambioContrasena();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        // Mostrar el menú principal nuevamente
        MenuAdmin menu = new MenuAdmin();
        menu.setVisible(true);
        this.dispose(); // Cierra solo este formulario
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        // TODO add your handling code here:
        filtrarHistorial();
    }//GEN-LAST:event_btnFiltrarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GestionContrasenaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GestionContrasenaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GestionContrasenaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GestionContrasenaForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GestionContrasenaForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tblHistorial;
    private javax.swing.JPasswordField txtConfirmacionHash;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JPasswordField txtHash;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtRealizadoPor;
    // End of variables declaration//GEN-END:variables
}
