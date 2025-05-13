package com.ahorrofamiliar.views;

import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.service.AuthService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame implements ActionListener {

    private JTextField dniField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private AuthService authService;

    public LoginView() {
        authService = new AuthService();

        setTitle("Ingreso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(4, 1, 10, 10)); // 4 filas: DNI, contraseña, botón, mensaje

        // Panel para el campo de DNI
        JPanel dniPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel dniLabel = new JLabel("DNI:");
        dniField = new JTextField(15);
        dniPanel.add(dniLabel);
        dniPanel.add(dniField);
        add(dniPanel);

        // Panel para el campo de contraseña
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(15);
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        add(passwordPanel);

        // Panel para el botón de inicio de sesión
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButton = new JButton("Ingresar");
        loginButton.addActionListener(this);
        buttonPanel.add(loginButton);
        add(buttonPanel);

        // Etiqueta para mostrar mensajes
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel);

        setVisible(true);
        setLocationRelativeTo(null); // Centrar la ventana
    }
    //Misael CHALLCO - INI 11/5 - SE AGREGA  METODOS PARA QUITAR REDUNDANCIA
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String dni = dniField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (dni.isEmpty() || password.isEmpty()) {
                mostrarMensaje("Por favor, ingrese DNI y contraseña.", Color.RED);
                return;
            }

            Usuario usuario = authService.autenticarUsuarioPorDni(dni, password);

            if (usuario != null) {
                mostrarMensaje("Ingreso exitoso como: " + usuario.getNombre() + " " + usuario.getApellido(), Color.GREEN);

                // Abrir menú según rol
                SwingUtilities.invokeLater(() -> {
                    switch (usuario.getIdRol()) {
                        case 1 ->
                            new MenuAdmin().setVisible(true);
                        case 2 ->
                            new MenuRol2().setVisible(true);
                        default ->
                            new MenuRol3().setVisible(true);
                    }
                    dispose();
                });

            } else {
                mostrarMensaje("Credenciales incorrectas.", Color.RED);
            }

            limpiarCampos();
        }
    }

    // Método para mostrar mensaje en la etiqueta con color personalizado
    private void mostrarMensaje(String mensaje, Color color) {
        messageLabel.setText(mensaje);
        messageLabel.setForeground(color);
    }

    private void limpiarCampos() {
        dniField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}


    //Misael CHALLCO - FIN 11/5 - SE AGREGA  METODOS PARA QUITAR REDUNDANCIA
