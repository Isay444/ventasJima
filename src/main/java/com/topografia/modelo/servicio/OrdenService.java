
package com.topografia.modelo.servicio;
import com.topografia.modelo.dao.OrdenRepository;
import com.topografia.modelo.entidades.Orden;
import java.util.List;

public class OrdenService {
    private final OrdenRepository repo = new OrdenRepository();
    
    public List<Orden> listar(){
        return repo.finAll();
    }
    
    public void guardar(Orden orden){
        repo.save(orden);
    }
    
    public void eliminar(Orden orden){
        repo.delete(orden);
    }
}
