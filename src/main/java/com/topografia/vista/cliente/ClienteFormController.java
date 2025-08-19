
package com.topografia.vista.cliente;

import com.topografia.modelo.entidades.Cliente;
import com.topografia.modelo.servicio.ClienteService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class ClienteFormController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    private Cliente cliente;
    private ClienteService service;
    private ClienteController clienteController;

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            txtNombre.setText(cliente.getNombre());
            txtDireccion.setText(cliente.getDireccion());
            txtTelefono.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
        }
    }

    public void setClienteService(ClienteService service) {
        this.service = service;
    }

    public void setClienteController(ClienteController controller) {
        this.clienteController = controller;
    }

    @FXML
    public void guardar() {
        if (txtNombre.getText().isEmpty() || txtDireccion.getText().isEmpty() ||
            txtTelefono.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            mostrarAlerta("Todos los campos son obligatorios");
            return;
        }


        if (cliente == null) {
            cliente = new Cliente();
        }
        cliente.setNombre(txtNombre.getText().toUpperCase());
        cliente.setDireccion(txtDireccion.getText().toUpperCase());
        cliente.setTelefono(txtTelefono.getText().toUpperCase());
        cliente.setEmail(txtEmail.getText());

        service.guardarCliente(cliente);
        clienteController.cargarClientes();
        cerrarVentana();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
}
