
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Municipio;
import jakarta.persistence.EntityManager;
import java.util.List;

public class MunicipioRepository {
    public List<Municipio> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Municipio m", Municipio.class).getResultList();
        } finally {
            em.close();
        }
    }
}
