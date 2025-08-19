
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.SubtipoTerreno;
import jakarta.persistence.EntityManager;
import java.util.List;


public class SubtipoTerrenoRepository {
    public List<SubtipoTerreno> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT st FROM SubtipoTerreno st", SubtipoTerreno.class).getResultList();
        } finally {
            em.close();
        }
    }
}
