
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Ingeniero;
import jakarta.persistence.EntityManager;
import java.util.List;

public class IngenieroRepository {
    public List<Ingeniero> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT i FROM Ingeniero i", Ingeniero.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public void delete(Ingeniero ingeniero) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            ingeniero = em.merge(ingeniero);
            em.remove(ingeniero);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
