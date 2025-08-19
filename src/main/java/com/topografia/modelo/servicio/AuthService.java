
package com.topografia.modelo.servicio;
import com.topografia.modelo.entidades.Usuario;
import com.topografia.modelo.dao.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {
    private final UsuarioRepository repo = new UsuarioRepository();
    
    public Usuario login(String nombre, String contraseniaPlano){
        Usuario u = repo.findByNombre(nombre);
        if (u == null){
            System.out.println("⚠️ Usuario no existe en DB.");
            return null;
        }
        System.out.println("Contraseña ingresada: " + contraseniaPlano);
    System.out.println("Hash en DB: " + u.getContraseniaHash());   
        
        //Validar hash
        boolean match = BCrypt.checkpw(contraseniaPlano, u.getContraseniaHash());
    System.out.println("Resultado BCrypt: " + match);

    if (match) {
        return u;
    }
    return null;      
    }
    
    // Útil para crear/actualizar usuarios
    public String hash(String contraseniaPlano) {
        return BCrypt.hashpw(contraseniaPlano, BCrypt.gensalt(10));
    }
}
