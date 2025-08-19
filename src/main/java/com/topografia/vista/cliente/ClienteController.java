
package com.topografia.vista.cliente;

import com.topografia.modelo.entidades.Cliente;
import com.topografia.modelo.servicio.ClienteService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

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

    private final ClienteService service = new ClienteService();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colDireccion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDireccion()));
        colTelefono.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTelefono()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        cargarClientes();
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
            mostrarAlerta("Seleccione un cliente para editar");
        }
    }

    @FXML
    public void eliminarCliente() {
        Cliente seleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            service.eliminarCliente(seleccionado);
            cargarClientes();
        } else {
            mostrarAlerta("Seleccione un cliente para eliminar");
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

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
