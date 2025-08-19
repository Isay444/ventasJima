package com.topografia.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;


public class MainApp extends Application{
    @Override
    public void start(Stage stage) throws Exception {     
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/login/Login.fxml"));
        if (loader.getLocation() == null) {
            System.out.println("No se encontró el archivo FXML.");
            return;
        }
        Scene scene = new Scene(loader.load());
        
        stage.setTitle("Topografía - Login");
        stage.setScene(scene);
        stage.show();
        
        //scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
    }
    
    public static void main(String[] args) {
        launch(args);
        /*String plainPassword = "admin123";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
        System.out.println("Hash generado: " + hashed);

        // Comprobar que funciona
        System.out.println("Verificación: " + BCrypt.checkpw("admin123", hashed));
*/
    }
}
