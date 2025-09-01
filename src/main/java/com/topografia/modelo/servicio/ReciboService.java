
package com.topografia.modelo.servicio;
import com.topografia.modelo.dao.ReciboRepository;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import com.topografia.utils.ValidadorRecibo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ReciboService {
    private final ReciboRepository repo = new ReciboRepository();
    private final OrdenService ordenService = new OrdenService();
    
    public List<Recibo> listar(){
        return repo.findAll();
    }
    
     public List<Recibo> listarPorOrden(Orden orden) {
        return repo.findByOrden(orden); 
    }
    @Transactional
    public void guardar(Recibo recibo) {
        ValidadorRecibo.validar(recibo);
        try {
            // 🔹 Recargar la orden con sus recibos antes de validar
            Orden ordenCompleta = ordenService.buscarPorIdConRecibos(recibo.getOrden().getId());

            // 🔹 Calcular total pagado actual (excluyendo este recibo si es edición)
            BigDecimal pagadoActual = ordenCompleta.getRecibos().stream()
                    .filter(r -> recibo.getId() == null || !r.getId().equals(recibo.getId()))
                    .filter(r -> Boolean.TRUE.equals(r.getConfirmado()))
                    .map(Recibo::getMonto)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 🔹 Sumar el monto del recibo que se intenta guardar si está confirmado
            if (Boolean.TRUE.equals(recibo.getConfirmado()) && recibo.getMonto() != null) {
                pagadoActual = pagadoActual.add(recibo.getMonto());
            }

            // 🔹 Validar que no exceda el monto total de la orden
            BigDecimal montoTotal = ordenCompleta.getMontoTotal() != null
                    ? ordenCompleta.getMontoTotal()
                    : BigDecimal.ZERO;

            if (pagadoActual.compareTo(montoTotal) > 0) {
                throw new IllegalArgumentException(
                        "La suma de los pagos (" + pagadoActual + ") excede el monto total de la orden (" + montoTotal + ")."
                );
            }
            // 🔹 Guardar recibo
            repo.save(recibo);

            // 🔹 Recalcular estado y fechas
            ordenService.actualizarEstadoYFechasPorPagos(ordenCompleta);
            System.out.println("Recibo guardado y orden actualizada correctamente");
        } catch (IllegalArgumentException e) {
            // Errores de validación controlados
            System.err.println("Error de validación al guardar recibo: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Errores inesperados
            System.err.println("Error al guardar recibo: " + e.getMessage());
            throw e;
        }
    }
       
    @Transactional
    public void eliminar(Recibo recibo) {
        if (recibo == null || recibo.getId() == null) {
            throw new IllegalArgumentException("Recibo inválido para eliminar");
        }
        try {
            Orden ordenCompleta = ordenService.buscarPorIdConRecibos(recibo.getOrden().getId());
            repo.delete(recibo);
            ordenService.actualizarEstadoYFechasPorPagos(ordenCompleta);

            System.out.println("Recibo eliminado y orden actualizada correctamente");
        } catch (Exception e) {
            System.err.println("Error al eliminar recibo: " + e.getMessage());
            throw e;
        }
    }
}
