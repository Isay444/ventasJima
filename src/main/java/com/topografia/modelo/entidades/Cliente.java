
package com.topografia.modelo.entidades;
import jakarta.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_clientes")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    //relacion con la orden (un cliente puede hacer muchas ordenes)
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Orden> ordenes;

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Orden> getOrdenes() { return ordenes; }
    public void setOrdenes(Set<Orden> ordenes) { this.ordenes = ordenes; }

    @Override
    public String toString() {
        return nombre;
    }
    
}
