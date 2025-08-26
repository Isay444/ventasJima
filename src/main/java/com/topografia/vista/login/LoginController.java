package com.topografia.vista.login;

import com.topografia.modelo.entidades.Usuario;
import com.topografia.modelo.servicio.AuthService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPass;
    @FXML private Button btnLogin;
    
    private AuthService auth = new AuthService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar eventos de teclado
        configurarEventosTeclado();
        
        // Configurar foco inicial
        Platform.runLater(() -> txtUsuario.requestFocus());
    }

    private void configurarEventosTeclado() {
        // Permitir login con Enter
        txtUsuario.setOnKeyPressed(this::manejarTeclaEnter);
        txtPass.setOnKeyPressed(this::manejarTeclaEnter);
        btnLogin.setOnKeyPressed(this::manejarTeclaEnter);
    }

    private void manejarTeclaEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    @FXML
    private void handleLogin() {
        String userInput = txtUsuario.getText().trim();
        String passInput = txtPass.getText().trim();

        // Validaciones básicas
        if (userInput.isEmpty()) {
            showAlert("Por favor, ingresa tu usuario", Alert.AlertType.WARNING);
            txtUsuario.requestFocus();
            return;
        }

        if (passInput.isEmpty()) {
            showAlert("Por favor, ingresa tu contraseña", Alert.AlertType.WARNING);
            txtPass.requestFocus();
            return;
        }

        // Deshabilitar botón durante el login
        btnLogin.setDisable(true);
        btnLogin.setText("Verificando...");

        try {
            System.out.println("LoginController -> Usuario introducido: " + userInput);

            Usuario usr = auth.login(userInput, passInput);
            if (usr != null) {
                System.out.println("Login correcto-> Rol: " + usr.getRol());
                abrirMain(usr);
            } else {
                System.out.println("Login fallido.");
                showAlert("Credenciales inválidas.\nVerifica tu usuario y contraseña.", Alert.AlertType.ERROR);
                limpiarCampos();
            }
        } catch (Exception e) {
            System.err.println("Error durante el login: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error del sistema.\nIntenta nuevamente.", Alert.AlertType.ERROR);
        } finally {
            // Restaurar botón
            btnLogin.setDisable(false);
            btnLogin.setText("INGRESAR");
        }
    }

    private void limpiarCampos() {
        txtPass.clear();
        txtUsuario.requestFocus();
    }

    private void abrirMain(Usuario usr) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/principal/Main.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Aplicar estilos CSS
            scene.getStylesheets().add(getClass().getResource("/styles/App.css").toExternalForm());
            
            Stage stage = new Stage();
            stage.setTitle("Topografía - Sistema Principal (" + usr.getRol() + ")");
            stage.setScene(scene);
            stage.setMaximized(true); // Abrir maximizada
            stage.show();

            // Cerrar ventana login
            Stage actual = (Stage) txtUsuario.getScene().getWindow();
            actual.close();
            
        } catch (Exception e) {
            System.err.println("Error al abrir la ventana principal: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error al abrir la ventana principal.\nContacta al administrador.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        
        // Personalizar título según el tipo
        switch (type) {
            case ERROR:
                alert.setTitle("Error");
                break;
            case WARNING:
                alert.setTitle("Advertencia");
                break;
            case INFORMATION:
                alert.setTitle("Información");
                break;
            default:
                alert.setTitle("Sistema");
        }
        
        alert.showAndWait();
    }
}