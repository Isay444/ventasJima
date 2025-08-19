
package com.topografia.modelo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "servicio")
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer id;
    
    @Column(nullable = false, length =100)
    private String nombre;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_servicio", nullable = false)
    private TipoServicio tipoServicio;
    
    @OneToMany(mappedBy = "servicio")
    private Set<Orden> ordenes;
    
    
//Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoServicio getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Set<Orden> getOrdenes() { return ordenes; }
    public void setOrdenes(Set<Orden> ordenes) { this.ordenes = ordenes; }
    
    @Override
    public String toString() { return nombre; }
      
}
