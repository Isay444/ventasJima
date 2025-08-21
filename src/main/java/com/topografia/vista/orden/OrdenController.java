    
package com.topografia.vista.orden;

import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.servicio.OrdenService;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OrdenController {
    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colFecha;
    @FXML private TableColumn<Orden, String> colEstado;
    @FXML private TableColumn<Orden, String> colCliente;
    @FXML private TableColumn<Orden, String> colServicio;
    @FXML private TableColumn<Orden, String> colIngeniero;
    @FXML private TableColumn<Orden, String> colObservaciones;
    @FXML private TableColumn<Orden, String> colUsuario;
    @FXML private TableColumn<Orden, String> colZonaEjidal;
    @FXML private TableColumn<Orden, String> colMunicipio;
    @FXML private TableColumn<Orden, String> colSubTerreno;
    
    private final OrdenService service = new OrdenService();
    
     @FXML
    public void initialize() {
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha().toString()));
        colEstado.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstado()));
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCliente().getNombre()));
        colServicio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getServicio().getNombre()));
        colIngeniero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIngeniero().getNombre()));
        colObservaciones.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getObservaciones()));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario().getNombre()));
        colZonaEjidal.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getZonaEjidal().getNombre()));
        colMunicipio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMunicipio().getNombre()));
        colSubTerreno.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSubtipoTerreno().getNombre()));

        cargarOrdenes();
    }
    
    @FXML
    public void cargarOrdenes() {
        tablaOrdenes.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void nuevaOrden() throws IOException {
        abrirFormulario(null);
    }

    @FXML
    public void editarOrden() throws IOException {
        Orden seleccionado = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Seleccione una orden");
        }
    }

    @FXML
    public void eliminarOrden() {
        Orden seleccionada = tablaOrdenes.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.setHeaderText("Eliminar Orden");
            confirmacion.setContentText("¿Está seguro de que desea eliminar esta orden?\n\n"
                    + "Cliente: " + seleccionada.getCliente().getNombre() + "\n"
                    + "Servicio: " + seleccionada.getServicio().getNombre());

            // Botones personalizados
            ButtonType botonSi = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
            ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmacion.getButtonTypes().setAll(botonSi, botonNo);

            // Mostrar y esperar respuesta
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if (respuesta == botonSi) {
                    service.eliminar(seleccionada);
                    cargarOrdenes();
                    mostrarAlerta("La orden fue eliminada correctamente.");
                }
            });

        } else {
            mostrarAlerta("Seleccione una orden para eliminar");
        }
        
    }
    
    @FXML
    private void abrirFormulario(Orden orden) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/orden/OrdenForm.fxml"));
        Scene scene = new Scene(loader.load());
        
        OrdenFormController controller = loader.getController();
        controller.setOrden(orden);
        controller.setOrdenService(service);
        controller.setOrdenController(this);
        
        Stage stage = new Stage();
        stage.setTitle(orden == null ? "Nueva Orden" : "Editar Orden");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
        

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    
}
