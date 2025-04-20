package com.ahorrofamiliar.views;

import com.ahorrofamiliar.models.Usuario;
import com.ahorrofamiliar.service.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame implements ActionListener {

    private JTextField nombreField;
    private JTextField apellidoField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;
    private AuthService authService;

    public LoginView() {
        authService = new AuthService();
        setTitle("Ingreso al Sistema");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 250); // Aumentamos la altura para el nuevo campo
        setLayout(new GridLayout(5, 1, 10, 10)); // 5 filas ahora

        // Panel para el campo de nombre
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField(15);
        nombrePanel.add(nombreLabel);
        nombrePanel.add(nombreField);
        add(nombrePanel);

        // Panel para el campo de apellido
        JPanel apellidoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel apellidoLabel = new JLabel("Apellido:");
        apellidoField = new JTextField(15);
        apellidoPanel.add(apellidoLabel);
        apellidoPanel.add(apellidoField);
        add(apellidoPanel);

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

        // Etiqueta para mostrar mensajes (éxito o error)
        messageLabel = new JLabel("");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(messageLabel);

        setVisible(true);
        setLocationRelativeTo(null); // Centrar la ventana
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String nombre = nombreField.getText();
            String apellido = apellidoField.getText();
            String password = new String(passwordField.getPassword());

            Usuario usuarioAutenticado = authService.autenticarUsuario(nombre, apellido, password);

            if (usuarioAutenticado != null) {
                messageLabel.setForeground(Color.GREEN);
                messageLabel.setText("Ingreso exitoso como: " + usuarioAutenticado.getNombre() + " " + usuarioAutenticado.getApellido());
                // Aquí podrías abrir la ventana principal de la aplicación
                // Por ahora, solo mostramos un mensaje
            } else {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText("Credenciales incorrectas.");
            }

            // Opcional: Limpiar los campos después del intento
            nombreField.setText("");
            apellidoField.setText("");
            passwordField.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView());
    }
}