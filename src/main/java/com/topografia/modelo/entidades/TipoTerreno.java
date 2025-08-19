
package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tipo_terreno")
public class TipoTerreno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_terreno")
    private Integer id;
    
    @Column(length = 50)
    private String nombre;
    
    @OneToMany(mappedBy = "tipoTerreno")
    private Set<SubtipoTerreno> subtipoTerrenos; //subtipos
    
    //getters setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<SubtipoTerreno> getSubtipoTerrenos() { return subtipoTerrenos; }
    public void setSubtipoTerrenos(Set<SubtipoTerreno> subtipoTerrenos) { this.subtipoTerrenos = subtipoTerrenos; }

    @Override
    public String toString() { return nombre; } 
}
