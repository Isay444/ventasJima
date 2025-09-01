package com.topografia.modelo.dao;
import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Orden;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class OrdenRepository {
    public List<Orden> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT DISTINCT o FROM Orden o LEFT JOIN FETCH o.recibos", Orden.class
            ).getResultList();
        } finally {
             em.close();
        }
    }
    
    public Orden findByIdConRecibos(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery(
                    "SELECT o FROM Orden o LEFT JOIN FETCH o.recibos WHERE o.id = :id", Orden.class
            ).setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    
    public Orden findById(Integer id) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.find(Orden.class, id);
        } finally {
            em.close();
        }
    }
    
    public void save(Orden orden){
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if(orden.getId() == null){
                em.persist(orden);
            } else {
                em.merge(orden);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Error guardando la orden", e);
        } finally {
            em.close(); 
        }
    }
            
    public void delete(Orden orden) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            
            //Verificar si la orden está adjunta antes de merge
            Orden managed = orden;
            if (!em.contains(orden)) {
                managed = em.find(Orden.class, orden.getId());
                if (managed == null) {
                    throw new RuntimeException("La orden con id=" + orden.getId() + " no existe");
                }
            }            
            em.remove(managed);
            em.flush(); // forzar ejecución inmediata (detecta FK/errores aquí)
            tx.commit();
            
            System.out.println("Orden eliminada exitosamente: ID=" + orden.getId());
            
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error eliminando orden: " + e.getMessage());
            throw new RuntimeException("Error eliminando la orden con id=" + orden.getId(), e);
        } finally {
            em.close();
        }
    }
}