
package com.topografia.vista.principal;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private BorderPane root;
    
    @FXML
    public void abrirClientes() {
        cargarVista("/vista/cliente/Cliente.fxml");
    }
    @FXML
    public void abrirOrdenes(){
        cargarVista("/vista/orden/Orden.fxml");
    }
    
    @FXML
    public void abrirRecibos(){
        cargarVista("/vista/recibo/Recibo.fxml");
    }
    
    @FXML
    private void cargarVista(String rutaFXML) {
        try {
            Parent contenido = FXMLLoader.load(getClass().getResource(rutaFXML));
            root.setCenter(contenido);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
