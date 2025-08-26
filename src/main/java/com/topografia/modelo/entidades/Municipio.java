
package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "municipios")
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id_municipios")
    private Integer id;
    
    @Column(length = 100)
    private String nombre;
    
    @OneToMany(mappedBy = "municipio")
    private Set<Orden> ordenes;
    
    //Relacion con CLiente??
    //@OneToMany(mappedBy = "municipio")
    //private Set<Cliente> cliente;
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<Orden> getOrdenes() { return ordenes; }

    public void setOrdenes(Set<Orden> ordenes) { this.ordenes = ordenes; }

    
    @Override
    public String toString() {
        return nombre;
    }
}
