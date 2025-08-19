
package com.topografia.modelo.dao;
import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Servicio;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ServicioRepository {
    public List<Servicio> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Servicio s", Servicio.class).getResultList();
        } finally {
            em.close();
        }
    }
}
