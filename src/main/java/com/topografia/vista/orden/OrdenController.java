package com.topografia.vista.orden;

import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.servicio.OrdenService;
import com.topografia.utils.TableFilter;
import com.topografia.vista.recibo.ReciboController;
import java.io.IOException;
import java.math.BigDecimal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.*;

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
    
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltro;
    
    @FXML private TableColumn<Orden, String> colSolicitaFactura;

    @FXML private TableColumn<Orden, String> colSaldoRestante;
    @FXML private TableColumn<Orden, String> colMontoTotal;
    @FXML private TableColumn<Orden, String> colFechaLevantamiento;
    @FXML private TableColumn<Orden, String> colFechaEntregaPlano;
    @FXML private TableColumn<Orden, String> colEstatus;

    @FXML private TableColumn<Orden, Void> colVerRecibos;

    private final OrdenService service = new OrdenService();
    private ObservableList<Orden> ordenes;
    private TableFilter<Orden> filtro;

    @FXML
    public void initialize() {
        ordenes = FXCollections.observableArrayList(service.listar());
        cbFiltro.getItems().addAll("Cliente", "Servicio", "Ingeniero", "Fecha", "Terreno", "Municipio", "Zona Ejidal", "Usuario", "Status", "Fecha Levantamiento");
        cbFiltro.setValue("Cliente");
        //Filtro para búsqueda
        filtro = new TableFilter<>(ordenes);
        filtro.conectar(
                txtBuscar,
                cbFiltro,
                tablaOrdenes,
                o -> o.getCliente().getNombre(),
                o -> o.getServicio().getNombre(),
                o -> o.getIngeniero().getNombre(),
                o -> o.getFecha().toString(),
                o -> o.getSubtipoTerreno().getNombre(),
                o -> o.getMunicipio().getNombre(),
                o -> o.getZonaEjidal().getNombre(),
                o -> o.getUsuario().getNombre(),
                o -> o.getEstatus().toString(),
                o -> o.getFechaLevantamiento().toString()
        );
        configurarColumnas();
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getId() != null ? c.getValue().getId().toString() : ""));
        colFecha.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFecha() != null ? c.getValue().getFecha().toString() : ""));
        colCliente.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCliente() != null ? c.getValue().getCliente().getNombre() : ""));
        colServicio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getServicio() != null ? c.getValue().getServicio().getNombre() : ""));
        colIngeniero.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIngeniero() != null ? c.getValue().getIngeniero().getNombre() : ""));
        colObservaciones.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getObservaciones() != null ? c.getValue().getObservaciones() : ""));
        colUsuario.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getUsuario() != null ? c.getValue().getUsuario().getNombre() : ""));
        colZonaEjidal.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getZonaEjidal() != null ? c.getValue().getZonaEjidal().getNombre() : ""));
        colMunicipio.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMunicipio() != null ? c.getValue().getMunicipio().getNombre() : ""));
        colSubTerreno.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getSubtipoTerreno() != null ? c.getValue().getSubtipoTerreno().getNombre() : ""));
        colMontoTotal.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getMontoTotal() != null ? c.getValue().getMontoTotal().toString() : ""));
        colFechaLevantamiento.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFechaLevantamiento() != null ? c.getValue().getFechaLevantamiento().toString() : ""));
        colFechaEntregaPlano.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFechaEntregaPlano() != null ? c.getValue().getFechaEntregaPlano().toString() : ""));
        colEstatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEstatus() != null ? c.getValue().getEstatus().toString() : ""));
        
        colSolicitaFactura.setCellValueFactory(c -> new SimpleStringProperty( Boolean.TRUE.equals(c.getValue().isSolicitaFactura()) ? "Sí" : "No" ));
        //colSaldoRestante.setCellValueFactory(c-> new SimpleStringProperty("$" + service.calcularSaldoRestante(c.getValue()).toPlainString()));
        colSaldoRestante.setCellValueFactory(c -> {
            BigDecimal saldo = service.calcularSaldoRestante(c.getValue());
            return new SimpleStringProperty(saldo != null ? "$" + saldo.toPlainString() : "");
        });

        colVerRecibos.setCellFactory(tc -> new TableCell<>() {
            private final Button btn = new Button("Ver Recibos");

            {
                btn.setOnAction(e -> {
                    Orden orden = getTableView().getItems().get(getIndex());
                    try {
                        abrirRecibosDeOrden(orden);
                    } catch (IOException ex) {
                        mostrarAlerta("Error abriendo recibos: " + ex.getMessage(), Alert.AlertType.ERROR);
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

    private void abrirRecibosDeOrden(Orden orden) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/recibo/Recibo.fxml"));
        Scene scene = new Scene(loader.load());

        ReciboController reciboController = loader.getController();
        reciboController.cargarRecibosDeOrden(orden); // nuevo método en ReciboController

        Stage stage = new Stage();
        stage.setTitle("Recibos de la Orden #" + orden.getId());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        // Al cerrar, recargar órdenes para reflejar saldo y estatus actualizados
        cargarOrdenes();
    }

    @FXML
    public void cargarOrdenes() {
        try {
            //tablaOrdenes.setItems(FXCollections.observableArrayList(service.listar()));
            ordenes.setAll(service.listar());
            System.out.println("Órdenes cargadas: " + tablaOrdenes.getItems().size() + " registros");
        } catch (Exception e) {
            System.err.println("Error cargando órdenes: " + e.getMessage());
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
        String mensaje = "¿Está seguro de eliminar esta ORDEN?"
                + "\nCliente: " + (seleccionada.getCliente() != null ? seleccionada.getCliente().getNombre() : "N/A")
                + "\nServicio: " + (seleccionada.getServicio() != null ? seleccionada.getServicio().getNombre() : "N/A")
                + "\nFecha: " + (seleccionada.getFecha() != null ? seleccionada.getFecha().toString() : "N/A")
                + "\t#Orden: " + seleccionada.getId() + "\nNota: Al eliminar esta orden también se eliminarán sus recibos relacionados.";

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar Eliminación");
        confirmacion.setHeaderText("Eliminar Orden");

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.YES) {
                try {
                    System.out.println("🗑️ Usuario confirmó eliminar orden ID=" + seleccionada.getId());

                    //Manejo de errores más robusto
                    service.eliminar(seleccionada);
                    cargarOrdenes();
                    mostrarAlerta("La orden fue eliminada correctamente", Alert.AlertType.INFORMATION);
                    System.out.println("Proceso de eliminación completado exitosamente");
                } catch (Exception e) {
                    System.err.println("Error eliminando orden en controlador: " + e.getMessage());
                    e.printStackTrace();
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
    private void abrirFormulario(Orden orden) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/orden/OrdenForm.fxml"));
        Scene scene = new Scene(loader.load());

        OrdenFormController controller = loader.getController();
        controller.setOrdenService(service);
        controller.setOrdenController(this);
        controller.setOrden(orden);

        Stage stage = new Stage();
        stage.setTitle(orden == null ? "Nueva Orden" : "Editar Orden");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void mostrarAlerta(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
