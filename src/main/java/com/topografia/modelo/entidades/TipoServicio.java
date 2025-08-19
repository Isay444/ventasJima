package com.topografia.modelo.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tipo_servicio")
public class TipoServicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_servicio")
    private Integer id;
    
    @Column(length = 50, name = "nombre")
    private String nombre;
    
    @OneToMany(mappedBy = "tipoServicio")
    private Set<Servicio> servicios;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<Servicio> servicios) {
        this.servicios = servicios;
    }



    @Override
    public String toString() { return nombre; }    
        
}
