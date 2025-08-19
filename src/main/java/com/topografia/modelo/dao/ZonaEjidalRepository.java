
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.ZonaEjidal;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ZonaEjidalRepository {
    public List<ZonaEjidal> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT z FROM ZonaEjidal z", ZonaEjidal.class).getResultList();
        } finally {
            em.close();
        }
    }
}
