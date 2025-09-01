
package com.topografia.vista.cliente;

import com.topografia.modelo.entidades.Cliente;
import com.topografia.modelo.servicio.ClienteService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.collections.ObservableList;
import com.topografia.utils.TableFilter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClienteController {
    @FXML private TableView<Cliente> tablaClientes;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colDireccion;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;
    
    @FXML private TableColumn<Cliente, String> colTipo;
    
    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbFiltro;
    private ObservableList<Cliente> clientes;
    private TableFilter<Cliente> filtro;

    private final ClienteService service = new ClienteService();

    @FXML
    public void initialize() {
        clientes = FXCollections.observableArrayList(service.listarClientes());
        cbFiltro.getItems().addAll("Nombre", "Direccion", "Telefono", "Email", "Tipo (Notaria, Particular)");
        cbFiltro.setValue("Aplicar filtro");
        
        filtro = new TableFilter<>(clientes);
        filtro.conectar(
                txtBuscar, 
                cbFiltro, 
                tablaClientes, 
                o -> o.getNombre(),
                o -> o.getDireccionCompleta(),
                o -> o.getTelefono(),
                o -> o.getEmail(),
                o -> o.getTipo().toString());
        
        configurarColumnas();
    }
    
    private void configurarColumnas(){
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()!= null ? c.getValue().getTipo().toString() : ""));
    }

    @FXML
    public void cargarClientes() {
        tablaClientes.setItems(FXCollections.observableArrayList(service.listarClientes()));
    }

    @FXML
    public void nuevoCliente() throws IOException {
        abrirFormulario(null);
    }

    @FXML
    public void editarCliente() throws IOException {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Seleccione un cliente para editar", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación de eliminación");
            confirmacion.setHeaderText("Eliminar Cliente");
            confirmacion.setContentText("¿Esta seguro de eliminar el cliente?\n\n"
                    + "Cliente: "+seleccionado.getNombre()+"\n"
                    + "ADVERTENCIA: Esta acción también eliminará todas las órdenes asociadas ");
            
            //botones para confirmar o cancelar
            ButtonType botonSi = new ButtonType("Sí", ButtonBar.ButtonData.OK_DONE);
            ButtonType botonNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            confirmacion.getButtonTypes().setAll(botonSi, botonNo);
            
            //mostrar y esperar respuesta
            confirmacion.showAndWait().ifPresent(respuesta -> {
                if(respuesta == botonSi){
                    service.eliminarCliente(seleccionado);
                    cargarClientes(); 
                    mostrarAlerta("El cliente y sus ordenes relacionadas fueron eliminados correctamente", Alert.AlertType.INFORMATION);
                }
            });
                     
        } else {
            mostrarAlerta("Seleccione un cliente para eliminar", Alert.AlertType.ERROR);
        }
    }

    private void abrirFormulario(Cliente cliente) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/cliente/ClienteForm.fxml"));
        Scene scene = new Scene(loader.load());

        ClienteFormController controller = loader.getController();
        controller.setCliente(cliente);
        controller.setClienteService(service);
        controller.setClienteController(this);

        Stage stage = new Stage();
        stage.setTitle(cliente == null ? "Nuevo Cliente" : "Editar Cliente");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    private void mostrarAlerta(String msg, Alert.AlertType type) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.setAlertType(type);
        alert.showAndWait();
    }
}
