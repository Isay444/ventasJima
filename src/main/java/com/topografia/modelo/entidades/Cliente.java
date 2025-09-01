package com.topografia.modelo.entidades;

import jakarta.persistence.*;
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

    // ✅ CAMPOS DE DIRECCIÓN SEPARADOS
    @Column(name = "localidad", nullable = true, length = 100)
    private String localidad;

    @Column(name = "colonia", nullable = true, length = 100)
    private String colonia;

    @Column(name = "calle_numero", nullable = true, length = 200)
    private String calleNumero;

    @Column(name = "codigo_postal", nullable = true, length = 10)
    private String codigoPostal;

    // ✅ RELACIÓN CON MUNICIPIO
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_municipio", nullable = true)
    private Municipio municipio;

    // ✅ CAMPO LEGACY - Mantener por compatibilidad (opcional)
    @Column(name = "direccion", nullable = true, length = 200)
    private String direccion;

    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @Column(name = "email", nullable = false, length = 100)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TIPOCLIENTE tipo = TIPOCLIENTE.Particular;
    
    public enum TIPOCLIENTE {
        Particular, Notaria
    }

    public TIPOCLIENTE getTipo() {
        return tipo;
    }

    public void setTipo(TIPOCLIENTE tipo) {
        this.tipo = tipo;
    }
    
    
    // Relación con órdenes
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Orden> ordenes;

    // ================================
    // GETTERS Y SETTERS EXISTENTES
    // ================================
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

    // ================================
    // NUEVOS GETTERS Y SETTERS
    // ================================
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    
    public String getColonia() { return colonia; }
    public void setColonia(String colonia) { this.colonia = colonia; }
    
    public String getCalleNumero() { return calleNumero; }
    public void setCalleNumero(String calleNumero) { this.calleNumero = calleNumero; }
    
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    
    public Municipio getMunicipio() { return municipio; }
    public void setMunicipio(Municipio municipio) { this.municipio = municipio; }

    // ================================
    // MÉTODOS DE UTILIDAD
    // ================================
    
    /**
     * Genera la dirección completa concatenando los campos separados
     */
    public String getDireccionCompleta() {
        StringBuilder direccionCompleta = new StringBuilder();
        
        if (calleNumero != null && !calleNumero.trim().isEmpty()) {
            direccionCompleta.append(calleNumero.trim());
        }
        
        if (colonia != null && !colonia.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(colonia.trim());
        }
        
        if (localidad != null && !localidad.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(localidad.trim());
        }
        
        if (municipio != null) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(", ");
            direccionCompleta.append(municipio.getNombre());
        }
        
        if (codigoPostal != null && !codigoPostal.trim().isEmpty()) {
            if (direccionCompleta.length() > 0) direccionCompleta.append(" ");
            direccionCompleta.append("CP: ").append(codigoPostal.trim());
        }
        
        return direccionCompleta.toString();
    }
    
    /**
     * Actualiza el campo direccion con la dirección completa
     * Útil para mantener compatibilidad con código existente
     */
    @PrePersist
    @PreUpdate
    public void actualizarDireccionCompleta() {
        this.direccion = getDireccionCompleta();
    }

    @Override
    public String toString() {
        return nombre;
    }
}