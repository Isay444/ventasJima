
package com.topografia.vista.orden;
import com.topografia.modelo.dao.*;
import com.topografia.modelo.entidades.*;
import com.topografia.modelo.entidades.Orden.EstatusOrden;
import com.topografia.modelo.servicio.OrdenService;
import java.math.BigDecimal;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import javafx.collections.FXCollections;
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
    
    @FXML private TextField txtMontoTotal;
    @FXML private Label lblSaldoRestante;
    @FXML private DatePicker dpFechaLevantamiento;
    @FXML private DatePicker dpFechaEntregaPlano;
    @FXML private ComboBox<EstatusOrden> cbEstatus;
    @FXML private CheckBox chkSolicitaFactura;

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
        cbEstatus.setItems(FXCollections.observableArrayList(EstatusOrden.values()));
        dpFecha.setValue(LocalDate.now()); // fecha por defecto
        
        chkSolicitaFactura.setSelected(true);
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
            
            chkSolicitaFactura.setSelected(orden.isSolicitaFactura());
            
            txtMontoTotal.setText(orden.getMontoTotal() != null ? orden.getMontoTotal().toPlainString() : "");
            BigDecimal saldo = service.calcularSaldoRestante(orden);
            lblSaldoRestante.setText("Saldo pendiente: $" + saldo.toPlainString());
            dpFechaLevantamiento.setValue(orden.getFechaLevantamiento());
            dpFechaEntregaPlano.setValue(orden.getFechaEntregaPlano());
            cbEstatus.setValue(orden.getEstatus());

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
        if (cbCliente.getValue() == null || cbServicio.getValue() == null || cbIngeniero.getValue() == null  || cbSubtipoTerreno.getValue() == null 
            || cbMunicipio.getValue() == null || cbZonaEjidal.getValue() == null || cbUsuario.getValue() == null || dpFecha.getValue() == null ||cbEstatus.getValue() == null) {
            mostrarAlerta("Algunos campos estan vacíos");
            return;
        }
        if (orden == null) {
            orden = new Orden();
            orden.setFecha(LocalDate.now());
        } 
        //Valida monto total
        try {
            BigDecimal monto = new BigDecimal(txtMontoTotal.getText());
            orden.setMontoTotal(monto);
        } catch (NumberFormatException e) {
            mostrarAlerta("Monto total inválido. Debe ser un número positivo.");
            return;
        }
        // Asignar valores
        orden.setCliente(cbCliente.getValue());
        orden.setServicio(cbServicio.getValue());
        orden.setIngeniero(cbIngeniero.getValue());
        orden.setSubtipoTerreno(cbSubtipoTerreno.getValue());
        orden.setMunicipio(cbMunicipio.getValue());
        orden.setZonaEjidal(cbZonaEjidal.getValue());
        orden.setUsuario(cbUsuario.getValue());
        orden.setFecha(dpFecha.getValue());
        orden.setObservaciones(txtObservaciones.getText().toUpperCase());
        
        orden.setSolicitaFactura(chkSolicitaFactura.isSelected());
        
        orden.setFechaLevantamiento(dpFechaLevantamiento.getValue());
        orden.setFechaEntregaPlano(dpFechaEntregaPlano.getValue());
        orden.setEstatus(cbEstatus.getValue());
        
        //Guardar
        service.guardar(orden);
        //Actualizar saldo
        BigDecimal saldo = service.calcularSaldoRestante(orden);
        lblSaldoRestante.setText("Saldo pendiente: $" + saldo.toPlainString());
        // Refrescar tabla y cerrar
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

