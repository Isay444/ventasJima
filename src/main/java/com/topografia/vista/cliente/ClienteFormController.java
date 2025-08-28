package com.topografia.vista.cliente;

import com.topografia.modelo.dao.MunicipioRepository;
import com.topografia.modelo.entidades.Cliente;
import com.topografia.modelo.entidades.Municipio;
import com.topografia.modelo.servicio.ClienteService;
import com.topografia.utils.Validador;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClienteFormController {
    
    // ================================
    // CAMPOS EXISTENTES
    // ================================
    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    // ================================
    // NUEVOS CAMPOS DE DIRECCIÓN
    // ================================
    @FXML private TextField txtCalleNumero;
    @FXML private TextField txtColonia;
    @FXML private TextField txtLocalidad;
    @FXML private ComboBox<Municipio> cbMunicipio;
    @FXML private TextField txtCodigoPostal;

    // ================================
    // DEPENDENCIAS
    // ================================
    private final MunicipioRepository municipioRepo = new MunicipioRepository();
    
    private Cliente cliente;
    private ClienteService service;
    private ClienteController clienteController;

    // ================================
    // INICIALIZACIÓN
    // ================================
    @FXML
    public void initialize() {
        // Cargar municipios en el ComboBox
        cbMunicipio.getItems().addAll(municipioRepo.findAll());
        
        // Configurar formato del ComboBox para mostrar solo el nombre
        cbMunicipio.setCellFactory(lv -> new ListCell<Municipio>() {
            @Override
            protected void updateItem(Municipio municipio, boolean empty) {
                super.updateItem(municipio, empty);
                setText(empty || municipio == null ? null : municipio.getNombre());
            }
        });
        
        cbMunicipio.setButtonCell(new ListCell<Municipio>() {
            @Override
            protected void updateItem(Municipio municipio, boolean empty) {
                super.updateItem(municipio, empty);
                setText(empty || municipio == null ? "Seleccione municipio..." : municipio.getNombre());
            }
        });

        // Validaciones en tiempo real (opcional)
        configurarValidacionesTiempoReal();
    }

    private void configurarValidacionesTiempoReal() {
        // Validar teléfono mientras escribe
        txtTelefono.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > 10) {
                txtTelefono.setText(oldVal); // Limitar a 10 dígitos
            }
        });

        // Validar código postal
        txtCodigoPostal.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && newVal.length() > 5) {
                txtCodigoPostal.setText(oldVal); // Limitar a 5 dígitos
            }
        });
    }

    // ================================
    // SETTERS PARA INYECCIÓN DE DEPENDENCIAS
    // ================================
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        if (cliente != null) {
            // Campos básicos
            txtNombre.setText(cliente.getNombre());
            txtTelefono.setText(cliente.getTelefono());
            txtEmail.setText(cliente.getEmail());
            
            // Campos de dirección
            txtCalleNumero.setText(cliente.getCalleNumero());
            txtColonia.setText(cliente.getColonia());
            txtLocalidad.setText(cliente.getLocalidad());
            txtCodigoPostal.setText(cliente.getCodigoPostal());
            
            // Municipio
            if (cliente.getMunicipio() != null) {
                cbMunicipio.setValue(cliente.getMunicipio());
            }
        }
    }

    public void setClienteService(ClienteService service) {
        this.service = service;
    }

    public void setClienteController(ClienteController controller) {
        this.clienteController = controller;
    }

    // ================================
    // ACCIONES
    // ================================
    @FXML
    public void guardar() {
        if (!validarFormulario()) {
            return;
        }

        try {
            if (cliente == null) {
                cliente = new Cliente();
            }

            // Asignar datos básicos
            cliente.setNombre(txtNombre.getText().trim().toUpperCase());
            cliente.setTelefono(txtTelefono.getText().trim());
            cliente.setEmail(txtEmail.getText().trim().toLowerCase());

            // Asignar datos de dirección
            cliente.setCalleNumero(txtCalleNumero.getText().trim().toUpperCase());
            cliente.setColonia(txtColonia.getText().trim().toUpperCase());
            cliente.setLocalidad(txtLocalidad.getText().trim().toUpperCase());
            cliente.setCodigoPostal(txtCodigoPostal.getText().trim());
            cliente.setMunicipio(cbMunicipio.getValue());

            // Guardar
            service.guardarCliente(cliente);
            clienteController.cargarClientes();
            
            mostrarMensaje("Cliente guardado exitosamente", Alert.AlertType.INFORMATION);
            cerrarVentana();

        } catch (Exception e) {
            System.err.println("Error guardando cliente: " + e.getMessage());
            mostrarMensaje("Error al guardar el cliente: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void cancelar() {
        cerrarVentana();
    }

    // ================================
    // VALIDACIONES
    // ================================
    private boolean validarFormulario() {
        // Campos obligatorios
        if (!Validador.validarTextoNoVacio(txtNombre.getText(), "Nombre")) return false;
        if (!Validador.validarTextoNoVacio(txtColonia.getText(), "Colonia")) return false;
        if (!Validador.validarTextoNoVacio(txtLocalidad.getText(), "Localidad")) return false;
        if (cbMunicipio.getValue() == null){
            mostrarMensaje("El campo Municipio es obligatorio", Alert.AlertType.NONE);
            return false;
        }
        if (!Validador.validarTelefono(txtTelefono.getText())) return false;
        if (!Validador.validarEmail(txtEmail.getText())) return false;

        // Validaciones específicas de dirección
        if (txtCodigoPostal.getText() != null && !txtCodigoPostal.getText().trim().isEmpty()) {
            if (!txtCodigoPostal.getText().matches("\\d{5}")) {
                mostrarMensaje("El código postal debe tener exactamente 5 dígitos", Alert.AlertType.WARNING);
                txtCodigoPostal.requestFocus();
                return false;
            }
        }

        return true;
    }

    // ================================
    // UTILIDADES
    // ================================
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarMensaje(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Cliente");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}