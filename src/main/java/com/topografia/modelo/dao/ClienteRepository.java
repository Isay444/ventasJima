
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Cliente;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ClienteRepository {

    public Cliente findById(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try { return em.find(Cliente.class, id); }
        finally { em.close(); }
    }
    
    public List<Cliente> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void save(Cliente cliente) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            if (cliente.getId() == null) {
                em.persist(cliente);
            } else {
                em.merge(cliente);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    
    public void delete(Cliente cliente) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            cliente = em.merge(cliente);
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
