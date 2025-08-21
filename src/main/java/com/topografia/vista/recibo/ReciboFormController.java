package com.topografia.vista.recibo;

import com.topografia.modelo.dao.OrdenRepository;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.servicio.ReciboService;
import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReciboFormController {

    @FXML
    private DatePicker dpFecha;
    @FXML
    private TextField txtMonto;
    @FXML
    private ComboBox<String> cbMetodoPago;

    private Recibo recibo;
    private ReciboService service;
    private ReciboController controller;

    private final OrdenRepository ordenRepo = new OrdenRepository();

    @FXML
    private TableView<Orden> tablaOrdenes;
    @FXML
    private TableColumn<Orden, String> colFechaOrden;
    @FXML
    private TableColumn<Orden, String> colClienteOrden;
    @FXML
    private TableColumn<Orden, String> colServicioOrden;
    @FXML
    private TableColumn<Orden, String> colEstadoOrden;
    @FXML
    private TextField txtBuscarOrden;
    @FXML
    private Label lblOrdenSeleccionada;

    private Orden ordenSeleccionada;

    @FXML
    public void initialize() {
        cbMetodoPago.getItems().addAll("Efectivo", "Transferencia", "Tarjeta");
        dpFecha.setValue(LocalDate.now());
        // Configurar columnas
        colFechaOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));
        colClienteOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        colServicioOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getServicio().getNombre()));
        colEstadoOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEstado()));

        // Cargar órdenes desde la DB
        ObservableList<Orden> ordenes = FXCollections.observableArrayList(new OrdenRepository().finAll());

        // Filtro dinámico
        FilteredList<Orden> filtradas = new FilteredList<>(ordenes, p -> true);
        txtBuscarOrden.textProperty().addListener((obs, old, nuevo) -> {
            filtradas.setPredicate(o -> {
                if (nuevo == null || nuevo.isEmpty()) {
                    return true;
                }
                String lower = nuevo.toLowerCase();
                return o.getCliente().getNombre().toLowerCase().contains(lower)
                        || o.getServicio().getNombre().toLowerCase().contains(lower)
                        || o.getEstado().toLowerCase().contains(lower)
                        || o.getFecha().toString().contains(lower);
            });
        });

        tablaOrdenes.setItems(filtradas);

        // Selección de orden
        tablaOrdenes.getSelectionModel().selectedItemProperty().addListener((obs, old, nueva) -> {
            ordenSeleccionada = nueva;
            if (nueva != null) {
                lblOrdenSeleccionada.setText("Orden seleccionada: "
                        + nueva.getCliente().getNombre() + " - " + nueva.getServicio().getNombre());
            } else {
                lblOrdenSeleccionada.setText("Orden seleccionada: Ninguna");
            }
        });
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
        if (recibo != null) {
            dpFecha.setValue(recibo.getFecha());
            txtMonto.setText(recibo.getMonto().toString());
            cbMetodoPago.setValue(recibo.getMetodo_pago());

            // En lugar de un comobobx, seleccionamos en la tabla
            ordenSeleccionada = recibo.getOrden();
            if (ordenSeleccionada != null) {
                tablaOrdenes.getSelectionModel().select(ordenSeleccionada);
                lblOrdenSeleccionada.setText("Orden seleccionada: "
                        + ordenSeleccionada.getCliente().getNombre()
                        + " - " + ordenSeleccionada.getServicio().getNombre());
            }
        }
    }

    public void setReciboService(ReciboService service) {
        this.service = service;
    }

    public void setReciboController(ReciboController controller) {
        this.controller = controller;
    }

    @FXML
    public void guardar() {
        
        if (ordenSeleccionada == null) {
        mostrarAlerta("Debes seleccionar una orden antes de guardar el recibo.");
        return;
        }
        if(txtMonto.getText().isEmpty() || cbMetodoPago.getValue().isEmpty() || dpFecha.getValue() == null || cbMetodoPago.getValue().isEmpty()){
            mostrarAlerta("Algunos campos son obligatorios");
            return;
        }
        if (recibo == null) {
            recibo = new Recibo();
        }
        recibo.setFecha(dpFecha.getValue());
        recibo.setMonto(new BigDecimal(txtMonto.getText()));
        recibo.setMetodo_pago(cbMetodoPago.getValue());
        recibo.setOrden(ordenSeleccionada);

        service.guardar(recibo);
        controller.cargarRecibos();
        cerrar();
    }

    @FXML
    public void cancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) dpFecha.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
