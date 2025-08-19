
package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "recibo")
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recibo")
    Integer id;
    
    @Column
    private LocalDate fecha;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
    
    @Column(nullable = false, length = 50)
    private String metodo_pago;
    
    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private Orden orden;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMetodo_pago() { return metodo_pago; }
    public void setMetodo_pago(String metodo_pago) { this.metodo_pago = metodo_pago; }
    public Orden getOrden() { return orden; }
    public void setOrden(Orden orden) { this.orden = orden; }
    
    
    
}
