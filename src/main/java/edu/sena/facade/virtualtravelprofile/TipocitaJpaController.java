/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sena.facade.virtualtravelprofile;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.sena.entity.virtualtravelprofile.Cita;
import edu.sena.entity.virtualtravelprofile.Tipocita;
import edu.sena.facade.virtualtravelprofile.exceptions.NonexistentEntityException;
import edu.sena.facade.virtualtravelprofile.exceptions.PreexistingEntityException;
import edu.sena.facade.virtualtravelprofile.exceptions.RollbackFailureException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Consola de Juegos
 */
public class TipocitaJpaController implements Serializable {

    public TipocitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipocita tipocita) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita idCita = tipocita.getIdCita();
            if (idCita != null) {
                idCita = em.getReference(idCita.getClass(), idCita.getIdCita());
                tipocita.setIdCita(idCita);
            }
            em.persist(tipocita);
            if (idCita != null) {
                idCita.getTipocitaCollection().add(tipocita);
                idCita = em.merge(idCita);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipocita(tipocita.getIptipoCita()) != null) {
                throw new PreexistingEntityException("Tipocita " + tipocita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipocita tipocita) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipocita persistentTipocita = em.find(Tipocita.class, tipocita.getIptipoCita());
            Cita idCitaOld = persistentTipocita.getIdCita();
            Cita idCitaNew = tipocita.getIdCita();
            if (idCitaNew != null) {
                idCitaNew = em.getReference(idCitaNew.getClass(), idCitaNew.getIdCita());
                tipocita.setIdCita(idCitaNew);
            }
            tipocita = em.merge(tipocita);
            if (idCitaOld != null && !idCitaOld.equals(idCitaNew)) {
                idCitaOld.getTipocitaCollection().remove(tipocita);
                idCitaOld = em.merge(idCitaOld);
            }
            if (idCitaNew != null && !idCitaNew.equals(idCitaOld)) {
                idCitaNew.getTipocitaCollection().add(tipocita);
                idCitaNew = em.merge(idCitaNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = tipocita.getIptipoCita();
                if (findTipocita(id) == null) {
                    throw new NonexistentEntityException("The tipocita with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipocita tipocita;
            try {
                tipocita = em.getReference(Tipocita.class, id);
                tipocita.getIptipoCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipocita with id " + id + " no longer exists.", enfe);
            }
            Cita idCita = tipocita.getIdCita();
            if (idCita != null) {
                idCita.getTipocitaCollection().remove(tipocita);
                idCita = em.merge(idCita);
            }
            em.remove(tipocita);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tipocita> findTipocitaEntities() {
        return findTipocitaEntities(true, -1, -1);
    }

    public List<Tipocita> findTipocitaEntities(int maxResults, int firstResult) {
        return findTipocitaEntities(false, maxResults, firstResult);
    }

    private List<Tipocita> findTipocitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipocita.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Tipocita findTipocita(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipocita.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipocitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipocita> rt = cq.from(Tipocita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
