
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
    
    @Column(name="metodo_pago", nullable = false, length = 50)
    private String metodoPago;
    
    @Column(name ="estado_pago", nullable = false)
    private String estadoPago; 
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal monto = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal anticipo = BigDecimal.ZERO;
    
    @Column(name ="anticipo_dos",precision = 10, scale = 2)
    private BigDecimal anticipoDos= BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal resto = BigDecimal.ZERO;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldo = BigDecimal.ZERO;
    
    @ManyToOne
    @JoinColumn(name = "id_orden", nullable = false)
    private Orden orden;

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

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public BigDecimal getAnticipo() { return anticipo; }
    public void setAnticipo(BigDecimal anticipo) { this.anticipo = anticipo; }
    public BigDecimal getAnticipoDos() { return anticipoDos; }
    public void setAnticipoDos(BigDecimal anticipoDos) { this.anticipoDos = anticipoDos; }
    public BigDecimal getResto() { return resto; }
    public void setResto(BigDecimal resto) { this.resto = resto; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    
    // Método para calcular resto y saldo automáticamente
    public void calcularTotales() {
        BigDecimal a1 = anticipo != null ? anticipo : BigDecimal.ZERO;
        BigDecimal a2 = anticipoDos != null ? anticipoDos : BigDecimal.ZERO;
        BigDecimal pagado = a1.add(a2);

        this.resto = monto.subtract(a1); // después del primer anticipo
        this.saldo = monto.subtract(pagado); // saldo final pendiente

        if (saldo.compareTo(BigDecimal.ZERO) == 0) {
            this.estadoPago = "PAGADO";
        } else if (saldo.compareTo(monto) == 0) {
            this.estadoPago = "PARCIAL";
        } else {
            this.estadoPago = "PENDIENTE";
        }
    }

    public void validarDatos() {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que 0");
        }
        if (anticipo != null && anticipo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El anticipo no puede ser negativo");
        }
        if (anticipoDos != null && anticipoDos.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El segundo anticipo no puede ser negativo");
        }

        BigDecimal totalPagado = (anticipo != null ? anticipo : BigDecimal.ZERO)
                .add(anticipoDos != null ? anticipoDos : BigDecimal.ZERO);

        if (totalPagado.compareTo(monto) > 0) {
            throw new IllegalArgumentException("La suma de anticipos no puede ser mayor al monto total");
        }
    }

}
