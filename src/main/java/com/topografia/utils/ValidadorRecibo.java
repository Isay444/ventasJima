package com.topografia.utils;

import com.topografia.modelo.entidades.Recibo;
import java.math.BigDecimal;

public class ValidadorRecibo {

    public static void validar(Recibo recibo) {
        if (recibo == null) {
            throw new IllegalArgumentException("El recibo no puede ser nulo.");
        }

        if (recibo.getFecha() == null) {
            throw new IllegalArgumentException("La fecha del recibo es obligatoria.");
        }

        if (recibo.getMonto() == null || recibo.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0.");
        }

        /*BigDecimal a1 = recibo.getAnticipo() != null ? recibo.getAnticipo() : BigDecimal.ZERO;
        BigDecimal a2 = recibo.getAnticipoDos() != null ? recibo.getAnticipoDos() : BigDecimal.ZERO;

        if (a1.compareTo(BigDecimal.ZERO) < 0 || a2.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Los anticipos no pueden ser negativos.");
        }

        if (a1.add(a2).compareTo(recibo.getMonto()) > 0) {
            throw new IllegalArgumentException("La suma de anticipos no puede superar al monto total.");
        }*/

        if (recibo.getMetodo_pago() == null || recibo.getMetodo_pago().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un método de pago.");
        }

        if (recibo.getOrden() == null) {
            throw new IllegalArgumentException("Debe asociar el recibo a una orden.");
        }
    }
}

