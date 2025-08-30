
package com.topografia.modelo.servicio;

import com.topografia.modelo.dao.ReciboRepository;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.utils.ValidadorRecibo;
import jakarta.transaction.Transactional;
import java.util.List;



public class ReciboService {
    private final ReciboRepository repo = new ReciboRepository();
    private final OrdenService ordenService = new OrdenService();
    
    public List<Recibo> listar(){
        return repo.findAll();
    }
    @Transactional
    public void guardar(Recibo recibo) {
        ValidadorRecibo.validar(recibo); //Utiliza una clase de /Utils para validaciones
        recibo.calcularTotales();   // Método para calcular resto y saldo automáticamente
        repo.save(recibo);
        ordenService.actualizarEstadoYFechasPorPagos(recibo.getOrden());
    }
    
    public void eliminar(Recibo recibo){
        repo.delete(recibo);
    }

}
