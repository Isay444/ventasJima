package com.topografia.modelo.servicio;

import com.topografia.modelo.dao.OrdenRepository;
import com.topografia.modelo.entidades.Orden;
import java.util.List;

public class OrdenService {
    private final OrdenRepository repo = new OrdenRepository();
    
    // ✅ CORREGIDO: finAll() -> findAll()
    public List<Orden> listar(){
        return repo.findAll();
    }
    
    public Orden buscarPorId(Integer id) {
        return repo.findById(id);
    }
    
    public void guardar(Orden orden){
        // ✅ AÑADIDO: Validación básica antes de guardar
        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede ser nula");
        }
        
        try {
            repo.save(orden);
            System.out.println("✅ Orden guardada exitosamente: " + 
                (orden.getId() != null ? "ID=" + orden.getId() : "Nueva orden"));
        } catch (Exception e) {
            System.err.println("❌ Error al guardar orden: " + e.getMessage());
            throw e;
        }
    }
    
    public void eliminar(Orden orden){
        // ✅ AÑADIDO: Validaciones antes de eliminar
        if (orden == null) {
            throw new IllegalArgumentException("La orden no puede ser nula");
        }
        
        if (orden.getId() == null) {
            throw new IllegalArgumentException("No se puede eliminar una orden sin ID");
        }
        
        try {
            System.out.println("🗑️ Intentando eliminar orden ID=" + orden.getId());
            repo.delete(orden);
            System.out.println("✅ Orden eliminada del servicio exitosamente");
        } catch (Exception e) {
            System.err.println("❌ Error en servicio al eliminar orden: " + e.getMessage());
            throw e;
        }
    }
}