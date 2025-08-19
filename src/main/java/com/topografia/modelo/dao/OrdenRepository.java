
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Orden;
import jakarta.persistence.EntityManager;
import java.util.List;

public class OrdenRepository {
    public List<Orden> finAll(){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Orden o", Orden.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public Orden findById(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        return em.find(Orden.class, id);
    }
    
    
    public void save(Orden orden){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            if(orden.getId()==null){
                em.persist(orden);
            }else{
                em.merge(orden);
            }
            em.getTransaction().commit();
        } finally {
            em.close(); 
        }
    }
            
    
    public void delete(Orden orden) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            orden = em.merge(orden);
            em.remove(orden);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
