
package com.topografia.modelo.entidades;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orden")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer id;
    
    @Column
    private LocalDate fecha;
    
    @Column(nullable = true, columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "monto_total", nullable = false)
    private BigDecimal montoTotal;

    @Column(name = "fecha_levantamiento")
    private LocalDate fechaLevantamiento;

    @Column(name = "fecha_entrega_plano")
    private LocalDate fechaEntregaPlano;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false)
    private EstatusOrden estatus = EstatusOrden.ACTIVA;
    
    public enum EstatusOrden {
        ACTIVA, CANCELADA, TERMINADA
    }
    
    @Column(name = "solicita_factura", nullable = false)
    private boolean solicitaFactura = false; // valor por defecto
    
    @Column(name = "requiere_plano", nullable = false)
    private boolean requierePlano = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_plano")
    private EstadoPlano estadoPlano; // Puede ser null si no requiere plano

    @Column(name = "ruta_plano")
    private String rutaPlano; // opcional, para guardar la ubicación del archivo

    public enum EstadoPlano {
        PENDIENTE,
        ENTREGADO
    }

    //relaciones
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_clientes", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "id_ingeniero", nullable = false)
    private Ingeniero ingeniero;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
    
    @ManyToOne
    @JoinColumn(name = "id_municipios", nullable = false)
    private Municipio municipio;
    
    @ManyToOne
    @JoinColumn(name = "id_subtipo_terreno", nullable = false)
    private SubtipoTerreno subtipo_terreno;
    
    @ManyToOne
    @JoinColumn(name = "id_servicio", nullable = false)
    private Servicio servicio;
    
    @ManyToOne
    @JoinColumn(name = "id_zona_ejidal", nullable = false)
    private ZonaEjidal zonaEjidal;
    
    @OneToMany(mappedBy = "orden", fetch = FetchType.LAZY)
    private Set<Recibo> recibos = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Ingeniero getIngeniero() { return ingeniero; }
    public void setIngeniero(Ingeniero ingeniero) { this.ingeniero = ingeniero; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Municipio getMunicipio() { return municipio; }
    public void setMunicipio(Municipio municipio) { this.municipio = municipio; }
    public SubtipoTerreno getSubtipoTerreno() { return subtipo_terreno; }
    public void setSubtipoTerreno(SubtipoTerreno subtipo_terreno) { this.subtipo_terreno = subtipo_terreno; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public ZonaEjidal getZonaEjidal() { return zonaEjidal; }
    public void setZonaEjidal(ZonaEjidal zonaEjidal) { this.zonaEjidal = zonaEjidal; }
    public Set<Recibo> getRecibos() { return recibos; }
    public void setRecibos(Set<Recibo> recibos) { this.recibos = recibos; }
    public SubtipoTerreno getSubtipo_terreno() { return subtipo_terreno; }
    public void setSubtipo_terreno(SubtipoTerreno subtipo_terreno) { this.subtipo_terreno = subtipo_terreno; }

    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    public LocalDate getFechaLevantamiento() { return fechaLevantamiento; }
    public void setFechaLevantamiento(LocalDate fechaLevantamiento) { this.fechaLevantamiento = fechaLevantamiento; }
    public LocalDate getFechaEntregaPlano() { return fechaEntregaPlano; }
    public void setFechaEntregaPlano(LocalDate fechaEntregaPlano) { this.fechaEntregaPlano = fechaEntregaPlano; }
    public EstatusOrden getEstatus() { return estatus; }
    public void setEstatus(EstatusOrden estatus) { this.estatus = estatus; }
    public boolean isSolicitaFactura() { return solicitaFactura; }
    public void setSolicitaFactura(boolean solicitaFactura) { this.solicitaFactura = solicitaFactura; }
    
    public boolean isRequierePlano() { return requierePlano; }
    public void setRequierePlano(boolean requierePlano) { this.requierePlano = requierePlano; }
    public EstadoPlano getEstadoPlano() { return estadoPlano; }
    public void setEstadoPlano(EstadoPlano estadoPlano) { this.estadoPlano = estadoPlano; }
    public String getRutaPlano() { return rutaPlano; }
    public void setRutaPlano(String rutaPlano) { this.rutaPlano = rutaPlano; }
}
