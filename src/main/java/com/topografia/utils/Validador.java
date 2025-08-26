
package com.topografia.utils;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;

public class Validador {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{10}$"); // Teléfono de 10 dígitos MX

    // =========================
    // Métodos de validación
    // =========================
    public static boolean validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            mostrarError("Correo inválido", "Por favor ingrese un correo válido.");
            return false;
        }
        return true;
    }

    public static boolean validarTelefono(String telefono) {
        if (telefono == null || !TELEFONO_PATTERN.matcher(telefono).matches()) {
            mostrarError("Teléfono inválido", "El teléfono debe tener 10 dígitos.");
            return false;
        }
        return true;
    }

    public static boolean validarTextoNoVacio(String campo, String nombreCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            mostrarError("Campo requerido", "El campo " + nombreCampo + " no puede estar vacío.");
            return false;
        }
        return true;
    }

    public static boolean validarNumeroPositivo(String numero, String nombreCampo) {
        try {
            double valor = Double.parseDouble(numero);
            if (valor <= 0) {
                mostrarError("Número inválido", "El campo " + nombreCampo + " debe ser mayor a 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            mostrarError("Número inválido", "El campo " + nombreCampo + " debe ser un número válido.");
            return false;
        }
    }

    // =========================
    // Método auxiliar para alertas
    // =========================
    private static void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
