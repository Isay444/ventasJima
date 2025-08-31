
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
    
    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private Orden orden;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto = BigDecimal.ZERO;
    
    @Column
    private LocalDate fecha;   
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pago", nullable = false)
    private TipoPago tipoPago; // ANTICIPO, SALDO, OTRO
    
    @Column(name = "confirmado", nullable = false)
    private Boolean confirmado = true; // Si el pago fue efectivo
    
    @Column(name="metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    public enum TipoPago {
        ANTICIPO, SALDO, OTRO
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getMetodo_pago() { return metodoPago; }
    public void setMetodo_pago(String metodo_pago) { this.metodoPago = metodo_pago; }
    public Orden getOrden() { return orden; }
    public void setOrden(Orden orden) { this.orden = orden; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }
}
