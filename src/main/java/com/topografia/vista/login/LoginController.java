
package com.topografia.vista.login;

import com.topografia.modelo.entidades.Usuario;
import com.topografia.modelo.servicio.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;


public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPass;
    
    private AuthService auth = new AuthService();

    @FXML
    private void handleLogin() {
        String userInput = txtUsuario.getText().trim();
    String passInput = txtPass.getText().trim();

    System.out.println("LoginController -> Usuario introducido: " + userInput + " | Contraseña introducida: " + passInput);

    Usuario usr = auth.login(userInput, passInput);
    if (usr != null) {
        System.out.println("Login correcto-> Rol: " + usr.getRol());
        abrirMain(usr);
    } else {
        System.out.println(" Login fallido.");
        showAlert("Credenciales inválidas", Alert.AlertType.ERROR);
    }
    }

    private void abrirMain(Usuario usr) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/principal/Main.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Topografía - Principal (" + usr.getRol() + ")");
            stage.setScene(scene);
            stage.show();

            // Cerrar ventana login
            Stage actual = (Stage) txtUsuario.getScene().getWindow();
            actual.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error al abrir la ventana principal", Alert.AlertType.ERROR);
            
        }
    }

    private void showAlert(String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}



