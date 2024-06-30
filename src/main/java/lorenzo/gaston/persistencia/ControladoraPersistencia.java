package lorenzo.gaston.persistencia;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lorenzo.gaston.logic.Personal;

/**
 * @author LORENZO
 */
public class ControladoraPersistencia {
    
    PersonalJpaController persoJpa = new PersonalJpaController();
    ComunaJpaController comunaJpa = new ComunaJpaController();
    DiagramasJpaController diagJpa = new DiagramasJpaController();
    LicenciasAnualesJpaController vacasJpa = new LicenciasAnualesJpaController();

    public void guardar(Personal personal){
    
        try {
            persoJpa.create(personal);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public List<Personal> traerPersona(int legajo) {
    
        return persoJpa.findPersonalEntities();
    
    }

    
    
    
}
