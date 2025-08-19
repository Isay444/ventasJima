
package com.topografia.modelo.entidades;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;
    
    @Column(name = "nombre", nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(name = "contrasenia", nullable = false, length = 60)
    private String contraseniaHash;

    @Column(name = "rol", nullable = false, length = 50)
    private String rol; // "Administrador" o "USER"
    
    @OneToMany  (mappedBy = "usuario")
    private Set<Orden> ordenes;
    
    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContraseniaHash() { return contraseniaHash; }
    public void setContraseniaHash(String contraseniaHash) { this.contraseniaHash = contraseniaHash; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Set<Orden> getOrdenes() { return ordenes; }
    public void setOrdenes(Set<Orden> ordenes) { this.ordenes = ordenes; }

    @Override
    public String toString() { return nombre; }
       
}
