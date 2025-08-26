
package com.topografia.vista.orden;

import com.topografia.modelo.dao.*;
import com.topografia.modelo.entidades.*;
import com.topografia.modelo.servicio.OrdenService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import javafx.stage.Stage;

public class OrdenFormController {

    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Servicio> cbServicio;
    @FXML private ComboBox<Ingeniero> cbIngeniero;
    @FXML private ComboBox<SubtipoTerreno> cbSubtipoTerreno;
    @FXML private ComboBox<Municipio> cbMunicipio;
    @FXML private ComboBox<ZonaEjidal> cbZonaEjidal;
    @FXML private ComboBox<Usuario> cbUsuario;

    @FXML private DatePicker dpFecha;
    @FXML private TextArea txtObservaciones;

    private final ClienteRepository clienteRepo = new ClienteRepository();
    private final ServicioRepository servicioRepo = new ServicioRepository();
    private final IngenieroRepository ingenieroRepo = new IngenieroRepository();
    private final SubtipoTerrenoRepository subtipoRepo = new SubtipoTerrenoRepository();
    private final MunicipioRepository municipioRepo = new MunicipioRepository();
    private final ZonaEjidalRepository zonaRepo = new ZonaEjidalRepository();
    private final UsuarioRepository usuarioRepo = new UsuarioRepository();
    
    private Orden orden;
    private OrdenService service;
    private OrdenController ordenController;
    
    @FXML
    public void initialize() {
        cbCliente.getItems().addAll(clienteRepo.findAll());
        cbServicio.getItems().addAll(servicioRepo.findAll());
        cbIngeniero.getItems().addAll(ingenieroRepo.findAll());
        cbSubtipoTerreno.getItems().addAll(subtipoRepo.findAll());
        cbMunicipio.getItems().addAll(municipioRepo.findAll());
        cbZonaEjidal.getItems().addAll(zonaRepo.findAll());
        cbUsuario.getItems().addAll(usuarioRepo.findAll());

        dpFecha.setValue(LocalDate.now()); // fecha por defecto
    }
    
    
    public void setOrden(Orden orden) {
        this.orden = orden;
        if (orden != null) {
            cbCliente.setValue(orden.getCliente());
            cbServicio.setValue(orden.getServicio());
            cbIngeniero.setValue(orden.getIngeniero());
            cbSubtipoTerreno.setValue(orden.getSubtipoTerreno());
            cbMunicipio.setValue(orden.getMunicipio());
            cbZonaEjidal.setValue(orden.getZonaEjidal());
            cbUsuario.setValue(orden.getUsuario());
            dpFecha.setValue(orden.getFecha());
        }
    }
    
    
    public void setOrdenService(OrdenService service) {
        this.service = service;
    }
    
    
    public void setOrdenController(OrdenController controller){
        this.ordenController = controller;
    }
    
    @FXML
    public void guardarOrden() {
        
        if (cbZonaEjidal.getItems().isEmpty() || cbUsuario.getItems().isEmpty() || 
            cbSubtipoTerreno.getItems().isEmpty() || cbCliente.getItems().isEmpty() || 
            cbIngeniero.getItems().isEmpty() || cbMunicipio.getItems().isEmpty() ||
            cbServicio.getItems().isEmpty() || dpFecha.getValue() == null) {
            mostrarAlerta("Algunos campos estan vacíos");
            return;
        }
        
        if (orden == null) {
            orden = new Orden();
            orden.setFecha(LocalDate.now());
        }
        
        orden.setCliente(cbCliente.getValue());
        orden.setServicio(cbServicio.getValue());
        orden.setIngeniero(cbIngeniero.getValue());
        orden.setSubtipoTerreno(cbSubtipoTerreno.getValue());
        orden.setMunicipio(cbMunicipio.getValue());
        orden.setZonaEjidal(cbZonaEjidal.getValue());
        orden.setUsuario(cbUsuario.getValue());
        orden.setFecha(dpFecha.getValue());
        
        orden.setObservaciones(txtObservaciones.getText().toUpperCase());

        service.guardar(orden);
        ordenController.cargarOrdenes();
        cerrarVentana();
    }

    @FXML
    public void cancelar() {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        Stage stage = (Stage) cbCliente.getScene().getWindow();
        stage.close();
    }
    
    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

