
package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Recibo;
import jakarta.persistence.EntityManager;
import java.util.List;


public class ReciboRepository {
    public List<Recibo> findAll(){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Recibo r", Recibo.class).getResultList();
        } finally {
            em.close();
        }
    }
    
    public Recibo findById(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        return em.find(Recibo.class, id);
    }
    
    public void save(Recibo recibo){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            if(recibo.getId()==null){
                em.persist(recibo);
            }else{
                em.merge(recibo);
            }
            em.getTransaction().commit();
        } finally {
            em.close(); 
        }
    }
            
    
    public void delete(Recibo recibo) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            recibo = em.merge(recibo);
            em.remove(recibo);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
