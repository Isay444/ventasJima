
package com.topografia.infra;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = build();
    
    private static EntityManagerFactory build() {
        try {
            return Persistence.createEntityManagerFactory("TopografiaPU");
        } catch (Exception ex) {
            throw new RuntimeException("Error inicializando JPA: " + ex.getMessage(), ex);
        }
    }
    
    public static EntityManagerFactory getEMF() {
        return emf;
    }

    public static void close() {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
