
package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "zona_ejidal")
public class ZonaEjidal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_zona_ejidal")
    private Integer id;
    
    @Column(length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "zonaEjidal")
    private Set<Orden> ordenes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Set<Orden> ordenes) {
        this.ordenes = ordenes;
    }

    @Override
    public String toString() { return nombre; }  
    
}
