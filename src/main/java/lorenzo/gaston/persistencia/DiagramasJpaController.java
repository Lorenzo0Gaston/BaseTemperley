package lorenzo.gaston.persistencia;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import lorenzo.gaston.logic.Diagramas;
import lorenzo.gaston.logic.Personal;
import lorenzo.gaston.persistencia.exceptions.NonexistentEntityException;
import lorenzo.gaston.persistencia.exceptions.PreexistingEntityException;



/**
 * @author LORENZO
 */
public class DiagramasJpaController implements Serializable {

    public DiagramasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public DiagramasJpaController(){
        emf = Persistence.createEntityManagerFactory("BasetemperleyPU");
    }
    
    public EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    public void create(Diagramas diagramas) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Personal personal = diagramas.getPersonal();
            if (personal != null) {
                personal = em.getReference(personal.getClass(), personal.getLegajo());
                diagramas.setPersonal(personal);
            }
            em.persist(diagramas);
            if (personal != null) {
                personal.getDiagramas().add(diagramas);
                personal = em.merge(personal);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDiagramas(diagramas.getDiagrama()) != null) {
                throw new PreexistingEntityException("Diagramas " + diagramas + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Diagramas diagramas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Diagramas persistentDiagramas = em.find(Diagramas.class, diagramas.getDiagrama());
            Personal personalOld = persistentDiagramas.getPersonal();
            Personal personalNew = diagramas.getPersonal();
            if (personalNew != null) {
                personalNew = em.getReference(personalNew.getClass(), personalNew.getLegajo());
                diagramas.setPersonal(personalNew);
            }
            diagramas = em.merge(diagramas);
            if (personalOld != null && !personalOld.equals(personalNew)) {
                personalOld.getDiagramas().remove(diagramas);
                personalOld = em.merge(personalOld);
            }
            if (personalNew != null && !personalNew.equals(personalOld)) {
                personalNew.getDiagramas().add(diagramas);
                personalNew = em.merge(personalNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = diagramas.getDiagrama();
                if (findDiagramas(id) == null) {
                    throw new NonexistentEntityException("The diagramas with id " + id + " no longer exists.");
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
            Diagramas diagramas;
            try {
                diagramas = em.getReference(Diagramas.class, id);
                diagramas.getDiagrama();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The diagramas with id " + id + " no longer exists.", enfe);
            }
            Personal personal = diagramas.getPersonal();
            if (personal != null) {
                personal.getDiagramas().remove(diagramas);
                personal = em.merge(personal);
            }
            em.remove(diagramas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Diagramas> findDiagramasEntities() {
        return findDiagramasEntities(true, -1, -1);
    }

    public List<Diagramas> findDiagramasEntities(int maxResults, int firstResult) {
        return findDiagramasEntities(false, maxResults, firstResult);
    }

    private List<Diagramas> findDiagramasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Diagramas.class));
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

    public Diagramas findDiagramas(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Diagramas.class, id);
        } finally {
            em.close();
        }
    }

    public int getDiagramasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Diagramas> rt = cq.from(Diagramas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
