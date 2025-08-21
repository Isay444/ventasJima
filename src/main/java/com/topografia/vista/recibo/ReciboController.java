
package com.topografia.vista.recibo;

import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.servicio.ReciboService;
import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

public class ReciboController {
    
    @FXML private TableView<Recibo> tablaRecibos;
    @FXML private TableColumn<Recibo, String> colFecha;
    @FXML private TableColumn<Recibo, String> colMonto;
    @FXML private TableColumn<Recibo, String> colMetodoPago;
    @FXML private TableColumn<Recibo, String> colOrden;
    
    private final ReciboService service = new ReciboService();
    
    @FXML
    public void initialize() {
        colFecha.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));
        colMonto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMonto().toString()));
        colMetodoPago.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMetodo_pago()));
        colOrden.setCellValueFactory(c -> new SimpleStringProperty("Orden #" + c.getValue().getOrden().getId()));
        cargarRecibos();
    }

    @FXML
    public void cargarRecibos() {
        tablaRecibos.setItems(FXCollections.observableArrayList(service.listar()));
    }

    @FXML
    public void nuevoRecibo() throws IOException {
        abrirFormulario(null);
    }

    @FXML
    public void editarRecibo() throws IOException {
        Recibo seleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Seleccione un recibo para editar");
        }
    }

    @FXML
    public void eliminarRecibo() {
        Recibo seleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar este recibo?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(respuesta -> {
                if (respuesta == ButtonType.YES) {
                    service.eliminar(seleccionado);
                    cargarRecibos();
                }
            });
        } else {
            mostrarAlerta("Seleccione un recibo para eliminar");
        }
    }

    private void abrirFormulario(Recibo recibo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/recibo/ReciboForm.fxml"));
        Scene scene = new Scene(loader.load());

        ReciboFormController controller = loader.getController();
        controller.setRecibo(recibo);
        controller.setReciboService(service);
        controller.setReciboController(this);

        Stage stage = new Stage();
        stage.setTitle(recibo == null ? "Nuevo Recibo" : "Editar Recibo");
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
