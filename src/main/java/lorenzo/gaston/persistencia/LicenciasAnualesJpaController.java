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
import lorenzo.gaston.logic.LicenciasAnuales;
import lorenzo.gaston.persistencia.exceptions.NonexistentEntityException;
import lorenzo.gaston.persistencia.exceptions.PreexistingEntityException;

/**
 * @author LORENZOS
 */
public class LicenciasAnualesJpaController implements Serializable {

    public LicenciasAnualesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public LicenciasAnualesJpaController(){
        emf = Persistence.createEntityManagerFactory("BaseTemperleyPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LicenciasAnuales licenciasAnuales) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(licenciasAnuales);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findLicenciasAnuales(licenciasAnuales.getMes()) != null) {
                throw new PreexistingEntityException("LicenciasAnuales " + licenciasAnuales + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LicenciasAnuales licenciasAnuales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            licenciasAnuales = em.merge(licenciasAnuales);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = licenciasAnuales.getMes();
                if (findLicenciasAnuales(id) == null) {
                    throw new NonexistentEntityException("The licenciasAnuales with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            LicenciasAnuales licenciasAnuales;
            try {
                licenciasAnuales = em.getReference(LicenciasAnuales.class, id);
                licenciasAnuales.getMes();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The licenciasAnuales with id " + id + " no longer exists.", enfe);
            }
            em.remove(licenciasAnuales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LicenciasAnuales> findLicenciasAnualesEntities() {
        return findLicenciasAnualesEntities(true, -1, -1);
    }

    public List<LicenciasAnuales> findLicenciasAnualesEntities(int maxResults, int firstResult) {
        return findLicenciasAnualesEntities(false, maxResults, firstResult);
    }

    private List<LicenciasAnuales> findLicenciasAnualesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LicenciasAnuales.class));
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

    public LicenciasAnuales findLicenciasAnuales(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LicenciasAnuales.class, id);
        } finally {
            em.close();
        }
    }

    public int getLicenciasAnualesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LicenciasAnuales> rt = cq.from(LicenciasAnuales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
