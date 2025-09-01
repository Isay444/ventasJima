package com.topografia.vista.recibo;

import com.topografia.modelo.dao.OrdenRepository;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.entidades.Recibo.TipoPago;
import com.topografia.modelo.servicio.OrdenService;
import com.topografia.modelo.servicio.ReciboService;
import com.topografia.utils.Validador;
import java.math.BigDecimal;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ReciboFormController {

    @FXML private DatePicker dpFecha;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> cbMetodoPago;
    
    @FXML private ComboBox<TipoPago> cbTipoPago;
    @FXML private CheckBox chkConfirmado;


    private Recibo recibo;
    private ReciboService service;
    private ReciboController controller;

    private final OrdenRepository ordenRepo = new OrdenRepository();

    @FXML private TableView<Orden> tablaOrdenes;
    @FXML private TableColumn<Orden, String> colFechaOrden;
    @FXML private TableColumn<Orden, String> colClienteOrden;
    @FXML private TableColumn<Orden, String> colServicioOrden;
    @FXML private TextField txtBuscarOrden;
    @FXML private Label lblOrdenSeleccionada;

    private Orden ordenSeleccionada;

    @FXML
    public void initialize() {
        cbMetodoPago.getItems().addAll("Efectivo", "Transferencia", "Tarjeta");
        dpFecha.setValue(LocalDate.now());
        
        cbTipoPago.setItems(FXCollections.observableArrayList(TipoPago.values()));
        chkConfirmado.setSelected(true);        
               
        // Configurar columnas para seleccionar la orden
        colFechaOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFecha().toString()));
        colClienteOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        colServicioOrden.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getServicio().getNombre()));

        // Cargar órdenes desde la DB
        ObservableList<Orden> ordenes = FXCollections.observableArrayList(new OrdenRepository().findAll());

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
            txtMonto.setText(recibo.getMonto() != null ? recibo.getMonto().toPlainString() : "");
            cbMetodoPago.setValue(recibo.getMetodo_pago());
            cbTipoPago.setValue(recibo.getTipoPago());
            chkConfirmado.setSelected(Boolean.TRUE.equals(recibo.getConfirmado()));

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
        if (cbTipoPago.getValue() == null) {
            mostrarAlerta("Debes seleccionar un tipo de pago.");
            return;
        }
        String monto = txtMonto.getText();
        String metodoPago = cbMetodoPago.getValue();
        String fecha = dpFecha.getValue().toString();        
        if (!Validador.validarTextoNoVacio(monto, "Monto")) return;
        if (!Validador.validarNumeroPositivo(monto, "Monto")) return;
        if (!Validador.validarTextoNoVacio(metodoPago, "Método de Pago")) return;
        if (!Validador.validarTextoNoVacio(fecha, "Fecha")) return;
        
        try {
            if (ordenSeleccionada == null) {
                mostrarAlerta("Debes seleccionar una orden antes de guardar el recibo.");
                return;
            }
             // 🔹 Recargar la orden con sus recibos inicializados
            OrdenService ordenService = new OrdenService();
            ordenSeleccionada = ordenService.buscarPorIdConRecibos(ordenSeleccionada.getId());
            
            boolean tieneAnticipo = ordenSeleccionada.getRecibos().stream()
                    .anyMatch(r -> r.getTipoPago() == TipoPago.ANTICIPO && Boolean.TRUE.equals(r.getConfirmado()));

            if (!tieneAnticipo && cbTipoPago.getValue() != TipoPago.ANTICIPO) {
                mostrarAlerta("Debe registrar al menos un pago como ANTICIPO antes de otros tipos de pago");
                return;
            }
            //Construir objeto
            if (recibo == null) recibo = new Recibo();
            
            recibo.setFecha(dpFecha.getValue());
            recibo.setMonto(new BigDecimal(txtMonto.getText()));
            recibo.setMetodo_pago(cbMetodoPago.getValue());
            recibo.setOrden(ordenSeleccionada);
            recibo.setTipoPago(cbTipoPago.getValue());
            recibo.setConfirmado(chkConfirmado.isSelected());

            // Guardar
            service.guardar(recibo);
            controller.cargarRecibos();
            cerrar();

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Error al guardar el recibo: " + e.getMessage());
        }
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
