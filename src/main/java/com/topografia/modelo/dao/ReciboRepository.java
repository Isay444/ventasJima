
package com.topografia.modelo.dao;
import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Orden;
import com.topografia.modelo.entidades.Recibo;
import jakarta.persistence.EntityManager;
import java.util.List;

public class ReciboRepository {
    public List<Recibo> findAll(){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
            "SELECT DISTINCT r FROM Recibo r " +
            "JOIN FETCH r.orden o " +
            "JOIN FETCH o.cliente", Recibo.class
        ).getResultList();

        } finally {
            em.close();
        }
    }
    
    public Recibo findById(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.find(Recibo.class, id);
        } finally {
            em.close();
        }
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
    
    public List<Recibo> findByOrden(Orden orden) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT r FROM Recibo r "
                    + "JOIN FETCH r.orden o "
                    + "LEFT JOIN FETCH o.recibos "
                    + "WHERE o = :orden", Recibo.class
            ).setParameter("orden", orden)
                    .getResultList();

        } finally {
            em.close();
        }
    }
}
