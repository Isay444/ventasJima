
package com.topografia.modelo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name= "ingeniero")
public class Ingeniero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingeniero")
    private Integer id;
    
    @Column(nullable = false, length =100)
    private String nombre;
    
    @Column(length = 100)
    private String especialidad;
    
    @Column(length = 15)
    private String telefono;
    
    @OneToMany  (mappedBy = "ingeniero")
    private Set<Orden> ordenes;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Set<Orden> getOrdenes() { return ordenes; }
    public void setOrdenes(Set<Orden> ordenes) { this.ordenes = ordenes; }
    @Override  
    public String toString(){ return nombre; } 
}
