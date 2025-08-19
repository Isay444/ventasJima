package com.topografia.modelo.dao;

import com.topografia.infra.JPAUtil;
import com.topografia.modelo.entidades.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;

public class UsuarioRepository {

    public Usuario findByNombre(String nombre) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            Usuario u = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.nombre = :nombre",
                    Usuario.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
            System.out.println("Usuario encontrado en DB: " + u.getNombre()
                    + "| Hash: " + u.getContraseniaHash());
            return u;
        } catch (NoResultException e) {
            System.out.println("⚠️ No se encontró usuario con nombre: " + nombre);
            return null;
        } finally {
            em.close();
        }

    }

    //////Guardar un nuevo usuario
    public void save(Usuario u) {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            em.getTransaction().begin();
            if (u.getId() == null) {
                em.persist(u);
            } else {
                u = em.merge(u);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
            throw ex;
        } finally {
            em.close();
        }

    }

    public List<Usuario> findAll() {
        EntityManager em = JPAUtil.getEMF().createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }
}
