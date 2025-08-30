package com.topografia.modelo.servicio;

import com.topografia.modelo.dao.OrdenRepository;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Orden.EstatusOrden;
import com.topografia.modelo.entidades.Recibo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import org.hibernate.Hibernate;

public class OrdenService {
    private final OrdenRepository repo = new OrdenRepository();
    
    // ✅ CORREGIDO: finAll() -> findAll()
    public List<Orden> listar(){
        return repo.findAll();
    }
    
    public Orden buscarPorId(Integer id) {
        return repo.findById(id);
    }
    @Transactional
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
    
    @Transactional
    public void actualizarEstadoYFechasPorPagos(Orden orden) {
        // Reatachar la orden para que esté en sesión
        Hibernate.initialize(orden.getRecibos());
        Orden ordenAdjunta = repo.findById(orden.getId());

        List<Recibo> recibos = orden.getRecibos()
                .stream()
                .sorted(Comparator.comparing(Recibo::getFecha))
                .toList();

        if (recibos.isEmpty()) {
            return;
        }

        // Fecha de levantamiento = primer anticipo > 0
        recibos.stream()
                .filter(r -> r.getAnticipo() != null && r.getAnticipo().compareTo(BigDecimal.ZERO) > 0)
                .findFirst()
                .ifPresent(r -> orden.setFechaLevantamiento(r.getFecha()));

        // Fecha de entrega plano = último pago con saldo 0
        recibos.stream()
                .filter(r -> r.getSaldo() != null && r.getSaldo().compareTo(BigDecimal.ZERO) == 0)
                .reduce((first, second) -> second)
                .ifPresent(r -> orden.setFechaEntregaPlano(r.getFecha()));

        // Estatus según pagos
        boolean pagado = recibos.stream()
                .anyMatch(r -> "PAGADO".equalsIgnoreCase(r.getEstadoPago()));

        if (pagado && orden.getFechaEntregaPlano() != null) {
            orden.setEstatus(EstatusOrden.TERMINADA);
        } else {
            orden.setEstatus(EstatusOrden.ACTIVA);
        }

        guardar(orden); // usa tu método existente
    }
}

