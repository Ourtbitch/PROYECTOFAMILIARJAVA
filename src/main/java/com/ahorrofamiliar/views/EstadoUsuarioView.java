package com.ahorrofamiliar.views;

// Importa tu clase de conexión - Misael Challco 12-05 - ini
import com.ahorrofamiliar.utils.DatabaseConnection; 
// Importa tu clase de conexión - Misael Challco 12-05 - FIN

import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class EstadoUsuarioView extends JFrame {
    private JTextField txtdocUsuario;
    private JLabel lblEstadoActual;
    private JButton btnConsultar, btnCambiarEstado, btnGuardar;
    private String estadoActual = "";

    public EstadoUsuarioView() {
        setTitle("Habilitar/Inhabilitar Usuario");
        setSize(400, 250);
        setLocationRelativeTo(null);
        // Cambié a 6 filas porque tienes 6 componentes - Misael Challco 12-05
        setLayout(new GridLayout(6, 1, 5, 5)); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        txtdocUsuario = new JTextField();
        lblEstadoActual = new JLabel("Estado actual: ");
        btnConsultar = new JButton("Consultar");
        btnCambiarEstado = new JButton("Cambiar Situacion");
        btnGuardar = new JButton("Guardar Cambios");

        add(new JLabel("Número de Documento Usuario:"));
        add(txtdocUsuario);
        add(btnConsultar);
        add(lblEstadoActual);
        add(btnCambiarEstado);
        add(btnGuardar);

        btnConsultar.addActionListener(e -> consultarEstado());
        btnCambiarEstado.addActionListener(e -> cambiarEstado());
        btnGuardar.addActionListener(e -> guardarEstado());

        setVisible(true);
    }

    private void consultarEstado() {
        String idUsuario = txtdocUsuario.getText().trim();
        if (idUsuario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa un número de documento.");
            return;
        }
        
        // Uso de DatabaseConnection.getConnection() para obtener la conexión - Misael Challco 12-05 - ini
        try (Connection con = DatabaseConnection.getConnection()) {
            String sql = "SELECT Situacion FROM usuario WHERE Numero_doc = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        estadoActual = rs.getString("Situacion");
                        lblEstadoActual.setText("Situacion actual: " + (estadoActual.equalsIgnoreCase("A") ? "Activo" : "Inactivo"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Usuario no encontrado");
                        estadoActual = "";
                        lblEstadoActual.setText("Estado actual: ");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al consultar la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        }
        // Uso de DatabaseConnection.getConnection() para obtener la conexión - Misael Challco 12-05 - FIN
    }

    private void cambiarEstado() {
        if (estadoActual.equalsIgnoreCase("A")) {
            estadoActual = "I";
        } else if (estadoActual.equalsIgnoreCase("I")) {
            estadoActual = "A";
        } else {
            JOptionPane.showMessageDialog(this, "Primero consulta un usuario válido.");
            return;
        }
        lblEstadoActual.setText("Situacion actual: " + (estadoActual.equalsIgnoreCase("A") ? "Activo" : "Inactivo"));
    }

    private void guardarEstado() {
        String docUsuario = txtdocUsuario.getText().trim();
        if (docUsuario.isEmpty() || estadoActual.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero consulta y cambia el estado del usuario.");
            return;
        }
        
        // Uso de DatabaseConnection.getConnection() para obtener la conexión - Misael Challco 12-05 - ini
        try (Connection con = DatabaseConnection.getConnection()) {
            String sql = "UPDATE usuario SET Situacion = ? WHERE Numero_doc = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, estadoActual);
                ps.setString(2, docUsuario);
                int res = ps.executeUpdate();
                if (res > 0) {
                    JOptionPane.showMessageDialog(this, "Situación actualizada correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar. Verifica el número de documento.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar la base de datos: " + ex.getMessage());
            ex.printStackTrace();
        }
        // Uso de DatabaseConnection.getConnection() para obtener la conexión - Misael Challco 12-05 - FIN
    }

    public static void main(String[] args) {
        // Mejora: Ejecutar en el hilo de eventos de Swing - Misael Challco 12-05
        javax.swing.SwingUtilities.invokeLater(() -> {
            new EstadoUsuarioView();
        });
    }
}
