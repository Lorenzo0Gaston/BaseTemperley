package lorenzo.gaston.logic;

import java.util.List;
import lorenzo.gaston.persistencia.ControladoraPersistencia;

/**
 * @author LENOVO
 */
public class Controladora {
    
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();
    
    

    public void guardar(String funcion, int orden, int legajo, String apellido, String nombre){
    
        Personal personal = new Personal();
        personal.setFuncion(funcion);
        personal.setOrden(orden);
        personal.setApellido(apellido);
        personal.setNombres(nombre);
        personal.setLegajo(legajo);        
    
        controlPersis.guardar(personal);
        
    }
    
    public List<Personal> traePersona(int legajo) {
        return controlPersis.traerPersona(legajo);
    }
    
    public List<Personal> traerPesona(){    
        return controlPersis.traerPersona();
    }
}

    
    

