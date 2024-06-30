package lorenzo.gaston.persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lorenzo.gaston.logic.Diagramas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import lorenzo.gaston.logic.Personal;
import lorenzo.gaston.persistencia.exceptions.NonexistentEntityException;
import lorenzo.gaston.persistencia.exceptions.PreexistingEntityException;

/**
 * @author LORENZO
 */
public class PersonalJpaController implements Serializable {

    public PersonalJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public PersonalJpaController(){
        emf = Persistence.createEntityManagerFactory("BaseTemperleyPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Personal personal) throws PreexistingEntityException, Exception {
        if (personal.getDiagramas() == null) {
            personal.setDiagramas(new ArrayList<Diagramas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Diagramas> attachedDiagramas = new ArrayList<Diagramas>();
            for (Diagramas diagramasDiagramasToAttach : personal.getDiagramas()) {
                diagramasDiagramasToAttach = em.getReference(diagramasDiagramasToAttach.getClass(), diagramasDiagramasToAttach.getDiagrama());
                attachedDiagramas.add(diagramasDiagramasToAttach);
            }
            personal.setDiagramas(attachedDiagramas);
            em.persist(personal);
            for (Diagramas diagramasDiagramas : personal.getDiagramas()) {
                Personal oldPersonalOfDiagramasDiagramas = diagramasDiagramas.getPersonal();
                diagramasDiagramas.setPersonal(personal);
                diagramasDiagramas = em.merge(diagramasDiagramas);
                if (oldPersonalOfDiagramasDiagramas != null) {
                    oldPersonalOfDiagramasDiagramas.getDiagramas().remove(diagramasDiagramas);
                    oldPersonalOfDiagramasDiagramas = em.merge(oldPersonalOfDiagramasDiagramas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPersonal(personal.getLegajo()) != null) {
                throw new PreexistingEntityException("Personal " + personal + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Personal personal) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal persistentPersonal = em.find(Personal.class, personal.getLegajo());
            List<Diagramas> diagramasOld = persistentPersonal.getDiagramas();
            List<Diagramas> diagramasNew = personal.getDiagramas();
            List<Diagramas> attachedDiagramasNew = new ArrayList<Diagramas>();
            for (Diagramas diagramasNewDiagramasToAttach : diagramasNew) {
                diagramasNewDiagramasToAttach = em.getReference(diagramasNewDiagramasToAttach.getClass(), diagramasNewDiagramasToAttach.getDiagrama());
                attachedDiagramasNew.add(diagramasNewDiagramasToAttach);
            }
            diagramasNew = attachedDiagramasNew;
            personal.setDiagramas(diagramasNew);
            personal = em.merge(personal);
            for (Diagramas diagramasOldDiagramas : diagramasOld) {
                if (!diagramasNew.contains(diagramasOldDiagramas)) {
                    diagramasOldDiagramas.setPersonal(null);
                    diagramasOldDiagramas = em.merge(diagramasOldDiagramas);
                }
            }
            for (Diagramas diagramasNewDiagramas : diagramasNew) {
                if (!diagramasOld.contains(diagramasNewDiagramas)) {
                    Personal oldPersonalOfDiagramasNewDiagramas = diagramasNewDiagramas.getPersonal();
                    diagramasNewDiagramas.setPersonal(personal);
                    diagramasNewDiagramas = em.merge(diagramasNewDiagramas);
                    if (oldPersonalOfDiagramasNewDiagramas != null && !oldPersonalOfDiagramasNewDiagramas.equals(personal)) {
                        oldPersonalOfDiagramasNewDiagramas.getDiagramas().remove(diagramasNewDiagramas);
                        oldPersonalOfDiagramasNewDiagramas = em.merge(oldPersonalOfDiagramasNewDiagramas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = personal.getLegajo();
                if (findPersonal(id) == null) {
                    throw new NonexistentEntityException("The personal with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal personal;
            try {
                personal = em.getReference(Personal.class, id);
                personal.getLegajo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The personal with id " + id + " no longer exists.", enfe);
            }
            List<Diagramas> diagramas = personal.getDiagramas();
            for (Diagramas diagramasDiagramas : diagramas) {
                diagramasDiagramas.setPersonal(null);
                diagramasDiagramas = em.merge(diagramasDiagramas);
            }
            em.remove(personal);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Personal> findPersonalEntities() {
        return findPersonalEntities(true, -1, -1);
    }

    public List<Personal> findPersonalEntities(int maxResults, int firstResult) {
        return findPersonalEntities(false, maxResults, firstResult);
    }

    private List<Personal> findPersonalEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Personal.class));
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

    public Personal findPersonal(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Personal.class, id);
        } finally {
            em.close();
        }
    }

    public int getPersonalCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Personal> rt = cq.from(Personal.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
