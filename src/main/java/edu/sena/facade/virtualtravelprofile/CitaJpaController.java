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
import edu.sena.entity.virtualtravelprofile.Cliente;
import edu.sena.entity.virtualtravelprofile.Asesor;
import edu.sena.entity.virtualtravelprofile.Cita;
import edu.sena.entity.virtualtravelprofile.Tipocita;
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
public class CitaJpaController implements Serializable {

    public CitaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cita cita) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cita.getTipocitaCollection() == null) {
            cita.setTipocitaCollection(new ArrayList<Tipocita>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cliente idClienteCita = cita.getIdClienteCita();
            if (idClienteCita != null) {
                idClienteCita = em.getReference(idClienteCita.getClass(), idClienteCita.getIdcliente());
                cita.setIdClienteCita(idClienteCita);
            }
            Asesor idAsesorCita = cita.getIdAsesorCita();
            if (idAsesorCita != null) {
                idAsesorCita = em.getReference(idAsesorCita.getClass(), idAsesorCita.getIdAsesor());
                cita.setIdAsesorCita(idAsesorCita);
            }
            Collection<Tipocita> attachedTipocitaCollection = new ArrayList<Tipocita>();
            for (Tipocita tipocitaCollectionTipocitaToAttach : cita.getTipocitaCollection()) {
                tipocitaCollectionTipocitaToAttach = em.getReference(tipocitaCollectionTipocitaToAttach.getClass(), tipocitaCollectionTipocitaToAttach.getIptipoCita());
                attachedTipocitaCollection.add(tipocitaCollectionTipocitaToAttach);
            }
            cita.setTipocitaCollection(attachedTipocitaCollection);
            em.persist(cita);
            if (idClienteCita != null) {
                idClienteCita.getCitaCollection().add(cita);
                idClienteCita = em.merge(idClienteCita);
            }
            if (idAsesorCita != null) {
                idAsesorCita.getCitaCollection().add(cita);
                idAsesorCita = em.merge(idAsesorCita);
            }
            for (Tipocita tipocitaCollectionTipocita : cita.getTipocitaCollection()) {
                Cita oldIdCitaOfTipocitaCollectionTipocita = tipocitaCollectionTipocita.getIdCita();
                tipocitaCollectionTipocita.setIdCita(cita);
                tipocitaCollectionTipocita = em.merge(tipocitaCollectionTipocita);
                if (oldIdCitaOfTipocitaCollectionTipocita != null) {
                    oldIdCitaOfTipocitaCollectionTipocita.getTipocitaCollection().remove(tipocitaCollectionTipocita);
                    oldIdCitaOfTipocitaCollectionTipocita = em.merge(oldIdCitaOfTipocitaCollectionTipocita);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCita(cita.getIdCita()) != null) {
                throw new PreexistingEntityException("Cita " + cita + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cita cita) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cita persistentCita = em.find(Cita.class, cita.getIdCita());
            Cliente idClienteCitaOld = persistentCita.getIdClienteCita();
            Cliente idClienteCitaNew = cita.getIdClienteCita();
            Asesor idAsesorCitaOld = persistentCita.getIdAsesorCita();
            Asesor idAsesorCitaNew = cita.getIdAsesorCita();
            Collection<Tipocita> tipocitaCollectionOld = persistentCita.getTipocitaCollection();
            Collection<Tipocita> tipocitaCollectionNew = cita.getTipocitaCollection();
            if (idClienteCitaNew != null) {
                idClienteCitaNew = em.getReference(idClienteCitaNew.getClass(), idClienteCitaNew.getIdcliente());
                cita.setIdClienteCita(idClienteCitaNew);
            }
            if (idAsesorCitaNew != null) {
                idAsesorCitaNew = em.getReference(idAsesorCitaNew.getClass(), idAsesorCitaNew.getIdAsesor());
                cita.setIdAsesorCita(idAsesorCitaNew);
            }
            Collection<Tipocita> attachedTipocitaCollectionNew = new ArrayList<Tipocita>();
            for (Tipocita tipocitaCollectionNewTipocitaToAttach : tipocitaCollectionNew) {
                tipocitaCollectionNewTipocitaToAttach = em.getReference(tipocitaCollectionNewTipocitaToAttach.getClass(), tipocitaCollectionNewTipocitaToAttach.getIptipoCita());
                attachedTipocitaCollectionNew.add(tipocitaCollectionNewTipocitaToAttach);
            }
            tipocitaCollectionNew = attachedTipocitaCollectionNew;
            cita.setTipocitaCollection(tipocitaCollectionNew);
            cita = em.merge(cita);
            if (idClienteCitaOld != null && !idClienteCitaOld.equals(idClienteCitaNew)) {
                idClienteCitaOld.getCitaCollection().remove(cita);
                idClienteCitaOld = em.merge(idClienteCitaOld);
            }
            if (idClienteCitaNew != null && !idClienteCitaNew.equals(idClienteCitaOld)) {
                idClienteCitaNew.getCitaCollection().add(cita);
                idClienteCitaNew = em.merge(idClienteCitaNew);
            }
            if (idAsesorCitaOld != null && !idAsesorCitaOld.equals(idAsesorCitaNew)) {
                idAsesorCitaOld.getCitaCollection().remove(cita);
                idAsesorCitaOld = em.merge(idAsesorCitaOld);
            }
            if (idAsesorCitaNew != null && !idAsesorCitaNew.equals(idAsesorCitaOld)) {
                idAsesorCitaNew.getCitaCollection().add(cita);
                idAsesorCitaNew = em.merge(idAsesorCitaNew);
            }
            for (Tipocita tipocitaCollectionOldTipocita : tipocitaCollectionOld) {
                if (!tipocitaCollectionNew.contains(tipocitaCollectionOldTipocita)) {
                    tipocitaCollectionOldTipocita.setIdCita(null);
                    tipocitaCollectionOldTipocita = em.merge(tipocitaCollectionOldTipocita);
                }
            }
            for (Tipocita tipocitaCollectionNewTipocita : tipocitaCollectionNew) {
                if (!tipocitaCollectionOld.contains(tipocitaCollectionNewTipocita)) {
                    Cita oldIdCitaOfTipocitaCollectionNewTipocita = tipocitaCollectionNewTipocita.getIdCita();
                    tipocitaCollectionNewTipocita.setIdCita(cita);
                    tipocitaCollectionNewTipocita = em.merge(tipocitaCollectionNewTipocita);
                    if (oldIdCitaOfTipocitaCollectionNewTipocita != null && !oldIdCitaOfTipocitaCollectionNewTipocita.equals(cita)) {
                        oldIdCitaOfTipocitaCollectionNewTipocita.getTipocitaCollection().remove(tipocitaCollectionNewTipocita);
                        oldIdCitaOfTipocitaCollectionNewTipocita = em.merge(oldIdCitaOfTipocitaCollectionNewTipocita);
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
                String id = cita.getIdCita();
                if (findCita(id) == null) {
                    throw new NonexistentEntityException("The cita with id " + id + " no longer exists.");
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
            Cita cita;
            try {
                cita = em.getReference(Cita.class, id);
                cita.getIdCita();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cita with id " + id + " no longer exists.", enfe);
            }
            Cliente idClienteCita = cita.getIdClienteCita();
            if (idClienteCita != null) {
                idClienteCita.getCitaCollection().remove(cita);
                idClienteCita = em.merge(idClienteCita);
            }
            Asesor idAsesorCita = cita.getIdAsesorCita();
            if (idAsesorCita != null) {
                idAsesorCita.getCitaCollection().remove(cita);
                idAsesorCita = em.merge(idAsesorCita);
            }
            Collection<Tipocita> tipocitaCollection = cita.getTipocitaCollection();
            for (Tipocita tipocitaCollectionTipocita : tipocitaCollection) {
                tipocitaCollectionTipocita.setIdCita(null);
                tipocitaCollectionTipocita = em.merge(tipocitaCollectionTipocita);
            }
            em.remove(cita);
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

    public List<Cita> findCitaEntities() {
        return findCitaEntities(true, -1, -1);
    }

    public List<Cita> findCitaEntities(int maxResults, int firstResult) {
        return findCitaEntities(false, maxResults, firstResult);
    }

    private List<Cita> findCitaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cita.class));
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

    public Cita findCita(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cita.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cita> rt = cq.from(Cita.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
