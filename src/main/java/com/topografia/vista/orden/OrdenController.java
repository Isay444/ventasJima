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
    @FXML private TableColumn<Orden, String> colId;
    @FXML private TableColumn<Orden, String> colFecha;
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
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId() != null ? c.getValue().getId().toString() : ""));
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""));        
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getCliente() != null ? c.getValue().getCliente().getNombre() : ""));
        colServicio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getServicio() != null ? c.getValue().getServicio().getNombre() : ""));
        colIngeniero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getIngeniero() != null ? c.getValue().getIngeniero().getNombre() : ""));
        colObservaciones.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getObservaciones() != null ? c.getValue().getObservaciones() : ""));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getUsuario() != null ? c.getValue().getUsuario().getNombre() : ""));
        colZonaEjidal.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getZonaEjidal() != null ? c.getValue().getZonaEjidal().getNombre() : ""));
        colMunicipio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getMunicipio() != null ? c.getValue().getMunicipio().getNombre() : ""));
        colSubTerreno.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty( c.getValue().getSubtipoTerreno() != null ? c.getValue().getSubtipoTerreno().getNombre() : ""));
        
        cargarOrdenes();
    }
    
    @FXML
    public void cargarOrdenes() {
        try {
            //System.out.println("🔄 Cargando órdenes...");
            tablaOrdenes.setItems(FXCollections.observableArrayList(service.listar()));
            //System.out.println("✅ Órdenes cargadas: " + tablaOrdenes.getItems().size() + " registros");
        } catch (Exception e) {
            //System.err.println("❌ Error cargando órdenes: " + e.getMessage());
            mostrarAlerta("Error cargando las órdenes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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
            mostrarAlerta("Seleccione una orden para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void eliminarOrden() {
        Orden seleccionada = tablaOrdenes.getSelectionModel().getSelectedItem();
        
        if (seleccionada == null) {
            mostrarAlerta("Seleccione una orden para eliminar", Alert.AlertType.WARNING);
            return;
        }

        // ✅ CORREGIDO: Cambio de "recibo" a "orden" en el mensaje
        String mensaje = "¿Está seguro de eliminar esta ORDEN?" +
                "\n\nCliente: " + (seleccionada.getCliente() != null ? seleccionada.getCliente().getNombre() : "N/A") +
                "\nServicio: " + (seleccionada.getServicio() != null ? seleccionada.getServicio().getNombre() : "N/A") +
                "\nFecha: " + (seleccionada.getFecha() != null ? seleccionada.getFecha().toString() : "N/A") +
                "\nID: " + seleccionada.getId();

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar Orden");
        
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    System.out.println("🗑️ Usuario confirmó eliminar orden ID=" + seleccionada.getId());
                    
                    // ✅ MEJORADO: Manejo de errores más robusto
                    service.eliminar(seleccionada);
                    
                    // Recargar la tabla
                    cargarOrdenes();
                    
                    // ✅ CORREGIDO: Mensaje de éxito específico para órdenes
                    mostrarAlerta("La orden fue eliminada correctamente", Alert.AlertType.INFORMATION);
                    
                    System.out.println("✅ Proceso de eliminación completado exitosamente");
                    
                } catch (Exception e) {
                    System.err.println("❌ Error eliminando orden en controlador: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Mostrar error específico al usuario
                    String errorMsg = "No se pudo eliminar la orden";
                    if (e.getMessage().contains("foreign key constraint")) {
                        errorMsg += ":\nExisten recibos asociados a esta orden. Elimine primero los recibos.";
                    } else {
                        errorMsg += ":\n" + e.getMessage();
                    }
                    
                    mostrarAlerta(errorMsg, Alert.AlertType.ERROR);
                }
            } else {
                System.out.println("❌ Usuario canceló la eliminación");
            }
        });
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

    // ✅ MEJORADO: Método más flexible para mostrar alertas
    private void mostrarAlerta(String msg) {
        mostrarAlerta(msg, Alert.AlertType.INFORMATION);
    }
    
    private void mostrarAlerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}