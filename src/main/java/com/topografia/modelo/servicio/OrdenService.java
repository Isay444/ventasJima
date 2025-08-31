package com.topografia.modelo.servicio;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.modelo.entidades.Orden.EstatusOrden;
import com.topografia.modelo.dao.OrdenRepository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class OrdenService {
    private final OrdenRepository repo = new OrdenRepository();

    public List<Orden> listar(){
        return repo.findAll();
    }
    
    public Orden buscarPorId(Integer id) {
        return repo.findById(id);
    }
    
    public void guardar(Orden orden){
        if (orden == null) throw new IllegalArgumentException("La orden no puede ser nula");       
        try {
            repo.save(orden);
            System.out.println("Orden guardada exitosamente: " + 
                (orden.getId() != null ? "ID=" + orden.getId() : "Nueva orden"));
        } catch (Exception e) {
            System.err.println("Error al guardar orden: " + e.getMessage());
            throw e;
        }
    }
    
    public void eliminar(Orden orden){
        if (orden == null) throw new IllegalArgumentException("La orden no puede ser nula");              
        if (orden.getId() == null) throw new IllegalArgumentException("No se puede eliminar una orden sin ID");
        try {
            System.out.println("Intentando eliminar orden ID=" + orden.getId());
            repo.delete(orden);
            System.out.println("Orden eliminada del servicio exitosamente");
        } catch (Exception e) {
            System.err.println("Error en servicio al eliminar orden: " + e.getMessage());
            throw e;
        }
    }
    
    public Orden buscarPorIdConRecibos(Integer id) {
        return repo.findByIdConRecibos(id);
    }
   
    @Transactional
    public void actualizarEstadoYFechasPorPagos(Orden orden) {
        if (orden == null || orden.getId() == null) return;
        // Reconsultar la orden con sus recibos ya cargados
        Orden ordenAdjunta = repo.findByIdConRecibos(orden.getId());

        var recibos = ordenAdjunta.getRecibos().stream()
                .filter(r -> Boolean.TRUE.equals(r.getConfirmado()))
                .sorted(Comparator.comparing(Recibo::getFecha))
                .toList();

        if (recibos.isEmpty()) {
            // Si no hay pagos confirmados, limpia fechas y status si aplica (opcional)
            // ordenAdjunta.setFechaLevantamiento(null);
            // ordenAdjunta.setFechaEntregaPlano(null);
            ordenAdjunta.setEstatus(Orden.EstatusOrden.ACTIVA);
            guardar(ordenAdjunta);
            return;
        }

        // Fecha de levantamiento = primer anticipo confirmado
        recibos.stream()
                .filter(r -> r.getTipoPago() == Recibo.TipoPago.ANTICIPO)
                .findFirst()
                .ifPresent(r -> ordenAdjunta.setFechaLevantamiento(r.getFecha()));

        // Fecha de entrega plano = último pago confirmado
        recibos.stream()
                .reduce((first, second) -> second)
                .ifPresent(r -> ordenAdjunta.setFechaEntregaPlano(r.getFecha()));

        // Calcular total pagado
        BigDecimal pagado = recibos.stream()
                .map(Recibo::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal montoTotal = ordenAdjunta.getMontoTotal() != null
                ? ordenAdjunta.getMontoTotal()
                : BigDecimal.ZERO;

        BigDecimal saldo = montoTotal.subtract(pagado);
        ordenAdjunta.setEstatus(saldo.compareTo(BigDecimal.ZERO) == 0
                ? EstatusOrden.TERMINADA
                : EstatusOrden.ACTIVA);

        guardar(ordenAdjunta);

    }
    
    public boolean puedeRealizarLevantamiento(Orden orden) {
        if (orden == null || orden.getRecibos() == null) return false;
        return orden.getRecibos().stream()
                .filter(r -> r.getMonto() != null && r.getMonto().compareTo(BigDecimal.ZERO) > 0)
                .anyMatch(r -> Boolean.TRUE.equals(r.getConfirmado()));
    }
    
    public BigDecimal calcularTotalPagado(Orden orden) {
        if (orden == null || orden.getRecibos() == null) return BigDecimal.ZERO;
        return orden.getRecibos().stream()
                .filter(r -> Boolean.TRUE.equals(r.getConfirmado()))
                .map(Recibo::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal calcularSaldoRestante(Orden orden) {
        if (orden == null) return BigDecimal.ZERO;

        BigDecimal montoTotal = orden.getMontoTotal() != null
                ? orden.getMontoTotal()
                : BigDecimal.ZERO;

        if (orden.getRecibos() == null || orden.getRecibos().isEmpty()) {
            return montoTotal; // si no hay recibos, saldo = monto total
        }

        BigDecimal pagado = orden.getRecibos().stream()
                .filter(r -> Boolean.TRUE.equals(r.getConfirmado()))
                .map(Recibo::getMonto)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return montoTotal.subtract(pagado);

    }
}