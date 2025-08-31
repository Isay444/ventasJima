
package com.topografia.vista.recibo;

import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.servicio.OrdenService;
import com.topografia.modelo.servicio.ReciboService;
import com.topografia.utils.TableFilter;
import com.topografia.vista.orden.OrdenFormController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

public class ReciboController {
    
    @FXML private TableView<Recibo> tablaRecibos;
    @FXML private TableColumn<Recibo, String> colFecha;
    @FXML private TableColumn<Recibo, String> colMonto;
    @FXML private TableColumn<Recibo, String> colTipoPago;
    @FXML private TableColumn<Recibo, String> colMetodoPago;
    @FXML private TableColumn<Recibo, String> colOrden;  
    
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltro;
    private ObservableList<Recibo> recibos;
    private TableFilter<Recibo> filtro;
    
    @FXML private TableColumn<Recibo, String> colConfirmado;
    @FXML private TableColumn<Recibo, Void> colVerOrden;
    @FXML private TableColumn<Recibo, String> colCliente;
    
    private final ReciboService service = new ReciboService();
    
    @FXML
    public void initialize() {
        recibos = FXCollections.observableArrayList(service.listar());
        cbFiltro.getItems().addAll("Fecha", "Metodo de Pago", "Tipo Pago");
        cbFiltro.setValue("Aplicar filtro");
        
        filtro = new TableFilter<>(recibos);
        filtro.conectar(
                txtBuscar,
                cbFiltro, 
                tablaRecibos,
                o -> o.getFecha().toString(),
                o -> o.getMetodo_pago(),
                o -> o.getTipoPago().toString()
        );
        configurarColumnas();
    }
    
    private void configurarColumnas(){
        colFecha.setCellValueFactory(c -> new SimpleStringProperty( c.getValue().getFecha() != null ? c.getValue().getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "" ));
        colMonto.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMonto().toString()));
        colTipoPago.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipoPago().toString()));
        colMetodoPago.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMetodo_pago()));
        colOrden.setCellValueFactory(c -> new SimpleStringProperty("Orden #" + c.getValue().getOrden().getId()));
        colConfirmado.setCellValueFactory(c -> new SimpleStringProperty( Boolean.TRUE.equals(c.getValue().getConfirmado()) ? "Sí" : "No" ));
        colCliente.setCellValueFactory(c -> new SimpleStringProperty( (c.getValue().getOrden() != null && c.getValue().getOrden().getCliente() != null) ? c.getValue().getOrden().getCliente().getNombre() : "" ));
        colVerOrden.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Ver Orden");

            {
                btn.setOnAction(e -> {
                    Recibo recibo = getTableView().getItems().get(getIndex());
                    if (recibo != null && recibo.getOrden() != null) {
                        try {
                            abrirOrdenDesdeRecibo(recibo.getOrden());
                        } catch (IOException ex) {
                            mostrarAlerta("Error abriendo la orden: " + ex.getMessage(), Alert.AlertType.ERROR);
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }
    
    private void abrirOrdenDesdeRecibo(Orden orden) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/orden/OrdenForm.fxml"));
    Scene scene = new Scene(loader.load());

    OrdenFormController ordenFormController = loader.getController();
    OrdenService ordenService = new OrdenService();

    // Recargar la orden con sus recibos ya cargados
    Orden ordenCompleta = ordenService.buscarPorIdConRecibos(orden.getId());

    ordenFormController.setOrdenService(ordenService);
    ordenFormController.setOrdenController(null); // si no necesitas refrescar tabla de órdenes aquí
    ordenFormController.setOrden(ordenCompleta);

    Stage stage = new Stage();
    stage.setTitle("Editar Orden #" + orden.getId());
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(scene);
    stage.showAndWait();
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
            mostrarAlerta("Seleccione un recibo para editar",  Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void eliminarRecibo() {
        Recibo seleccionado = tablaRecibos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccione un recibo para eliminar", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Está seguro de eliminar este recibo?", ButtonType.YES, ButtonType.NO);
        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    service.eliminar(seleccionado);
                    cargarRecibos();
                    mostrarAlerta("Recibo eliminado correctamente", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error al eliminar el recibo: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
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

    private void mostrarAlerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    public void cargarRecibosDeOrden(Orden orden) {
        recibos.setAll(service.listarPorOrden(orden)); // método nuevo en ReciboService
    }
    
}
