
package com.topografia.modelo.servicio;
import com.topografia.modelo.dao.ReciboRepository;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.entidades.Recibo.TipoPago;
import com.topografia.utils.ValidadorRecibo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;


public class ReciboService {
    private final ReciboRepository repo = new ReciboRepository();
    private final OrdenService ordenService = new OrdenService();
    
    public List<Recibo> listar(){
        return repo.findAll();
    }
    @Transactional
    public void guardar(Recibo recibo) {
        ValidadorRecibo.validar(recibo);
        //if (recibo.getTipoPago() == TipoPago.ANTICIPO && recibo.getMonto().compareTo(BigDecimal.ZERO) > 0) {
         //   recibo.getOrden().setFechaLevantamiento(recibo.getFecha()); }         
        repo.save(recibo);
        ordenService.actualizarEstadoYFechasPorPagos(recibo.getOrden());
    }
    @Transactional
    public void eliminar(Recibo recibo){
        if (recibo == null || recibo.getId() == null)
            throw new IllegalArgumentException("Recibo inválido para eliminar");
        Orden orden = recibo.getOrden(); // guardar referencia antes de borrar
        repo.delete(recibo);
        ordenService.actualizarEstadoYFechasPorPagos(orden);
    }
    
    public List<Recibo> listarPorOrden(Orden orden) {
        return repo.findByOrden(orden); // método en tu repositorio
    }
}
