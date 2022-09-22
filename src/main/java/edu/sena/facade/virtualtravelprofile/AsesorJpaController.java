/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sena.facade.virtualtravelprofile;

import edu.sena.entity.virtualtravelprofile.Asesor;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import edu.sena.entity.virtualtravelprofile.Cita;
import edu.sena.facade.virtualtravelprofile.exceptions.NonexistentEntityException;
import edu.sena.facade.virtualtravelprofile.exceptions.PreexistingEntityException;
import edu.sena.facade.virtualtravelprofile.exceptions.RollbackFailureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Consola de Juegos
 */
public class AsesorJpaController implements Serializable {

    public AsesorJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Asesor asesor) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (asesor.getCitaCollection() == null) {
            asesor.setCitaCollection(new ArrayList<Cita>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Cita> attachedCitaCollection = new ArrayList<Cita>();
            for (Cita citaCollectionCitaToAttach : asesor.getCitaCollection()) {
                citaCollectionCitaToAttach = em.getReference(citaCollectionCitaToAttach.getClass(), citaCollectionCitaToAttach.getIdCita());
                attachedCitaCollection.add(citaCollectionCitaToAttach);
            }
            asesor.setCitaCollection(attachedCitaCollection);
            em.persist(asesor);
            for (Cita citaCollectionCita : asesor.getCitaCollection()) {
                Asesor oldIdAsesorCitaOfCitaCollectionCita = citaCollectionCita.getIdAsesorCita();
                citaCollectionCita.setIdAsesorCita(asesor);
                citaCollectionCita = em.merge(citaCollectionCita);
                if (oldIdAsesorCitaOfCitaCollectionCita != null) {
                    oldIdAsesorCitaOfCitaCollectionCita.getCitaCollection().remove(citaCollectionCita);
                    oldIdAsesorCitaOfCitaCollectionCita = em.merge(oldIdAsesorCitaOfCitaCollectionCita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findAsesor(asesor.getIdAsesor()) != null) {
                throw new PreexistingEntityException("Asesor " + asesor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Asesor asesor) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Asesor persistentAsesor = em.find(Asesor.class, asesor.getIdAsesor());
            Collection<Cita> citaCollectionOld = persistentAsesor.getCitaCollection();
            Collection<Cita> citaCollectionNew = asesor.getCitaCollection();
            Collection<Cita> attachedCitaCollectionNew = new ArrayList<Cita>();
            for (Cita citaCollectionNewCitaToAttach : citaCollectionNew) {
                citaCollectionNewCitaToAttach = em.getReference(citaCollectionNewCitaToAttach.getClass(), citaCollectionNewCitaToAttach.getIdCita());
                attachedCitaCollectionNew.add(citaCollectionNewCitaToAttach);
            }
            citaCollectionNew = attachedCitaCollectionNew;
            asesor.setCitaCollection(citaCollectionNew);
            asesor = em.merge(asesor);
            for (Cita citaCollectionOldCita : citaCollectionOld) {
                if (!citaCollectionNew.contains(citaCollectionOldCita)) {
                    citaCollectionOldCita.setIdAsesorCita(null);
                    citaCollectionOldCita = em.merge(citaCollectionOldCita);
                }
            }
            for (Cita citaCollectionNewCita : citaCollectionNew) {
                if (!citaCollectionOld.contains(citaCollectionNewCita)) {
                    Asesor oldIdAsesorCitaOfCitaCollectionNewCita = citaCollectionNewCita.getIdAsesorCita();
                    citaCollectionNewCita.setIdAsesorCita(asesor);
                    citaCollectionNewCita = em.merge(citaCollectionNewCita);
                    if (oldIdAsesorCitaOfCitaCollectionNewCita != null && !oldIdAsesorCitaOfCitaCollectionNewCita.equals(asesor)) {
                        oldIdAsesorCitaOfCitaCollectionNewCita.getCitaCollection().remove(citaCollectionNewCita);
                        oldIdAsesorCitaOfCitaCollectionNewCita = em.merge(oldIdAsesorCitaOfCitaCollectionNewCita);
                    }
                }
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
                String id = asesor.getIdAsesor();
                if (findAsesor(id) == null) {
                    throw new NonexistentEntityException("The asesor with id " + id + " no longer exists.");
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
            Asesor asesor;
            try {
                asesor = em.getReference(Asesor.class, id);
                asesor.getIdAsesor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The asesor with id " + id + " no longer exists.", enfe);
            }
            Collection<Cita> citaCollection = asesor.getCitaCollection();
            for (Cita citaCollectionCita : citaCollection) {
                citaCollectionCita.setIdAsesorCita(null);
                citaCollectionCita = em.merge(citaCollectionCita);
            }
            em.remove(asesor);
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

    public List<Asesor> findAsesorEntities() {
        return findAsesorEntities(true, -1, -1);
    }

    public List<Asesor> findAsesorEntities(int maxResults, int firstResult) {
        return findAsesorEntities(false, maxResults, firstResult);
    }

    private List<Asesor> findAsesorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Asesor.class));
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

    public Asesor findAsesor(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Asesor.class, id);
        } finally {
            em.close();
        }
    }

    public int getAsesorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Asesor> rt = cq.from(Asesor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
