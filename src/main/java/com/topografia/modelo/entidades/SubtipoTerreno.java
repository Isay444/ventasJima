
package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subtipo_terreno")
public class SubtipoTerreno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subtipo_terreno")
    private Integer id;
    
    @Column(length = 50)
    private String nombre;
    
    @ManyToOne
    @JoinColumn(name = "id_tipo_terreno", nullable = false)
    private TipoTerreno tipoTerreno;
    
    @OneToMany(mappedBy = "subtipo_terreno")
    private Set<Orden> ordenes;
    
    //getters setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoTerreno getTipoTerreno() {
        return tipoTerreno;
    }

    public void setTipoTerreno(TipoTerreno tipoTerreno) {
        this.tipoTerreno = tipoTerreno;
    }

    public Set<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(Set<Orden> ordenes) {
        this.ordenes = ordenes;
    }
   

    @Override
    public String toString() { return nombre; } 
    
}
