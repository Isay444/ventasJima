
package com.topografia.modelo.servicio;

import com.topografia.modelo.dao.ReciboRepository;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.utils.ValidadorRecibo;
import java.util.List;


public class ReciboService {
    private final ReciboRepository repo = new ReciboRepository();
    
    public List<Recibo> listar(){
        return repo.findAll();
    }
    
    public void guardar(Recibo recibo) {
        ValidadorRecibo.validar(recibo); 
        recibo.calcularTotales();
        repo.save(recibo);
    }
    
    public void eliminar(Recibo recibo){
        repo.delete(recibo);
    }
}
