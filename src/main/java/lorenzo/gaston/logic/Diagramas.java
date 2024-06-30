package lorenzo.gaston.logic;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author LORENZO
 */
@Entity
public class Diagramas implements Serializable {

    @Id
    private int diagrama;
    
    private String diesel;
    private String electrico;
    
    @ManyToOne
    @JoinColumn(name = "personal_legajo")
    private Personal personal;

    public Diagramas() {
    }

    public Diagramas(int diagrama, String diesel, String electrico, Personal personal) {
        this.diagrama = diagrama;
        this.diesel = diesel;
        this.electrico = electrico;
        this.personal = personal;
    }

    public int getDiagrama() {
        return diagrama;
    }

    public void setDiagrama(int diagrama) {
        this.diagrama = diagrama;
    }

    public String getDiesel() {
        return diesel;
    }

    public void setDiesel(String diesel) {
        this.diesel = diesel;
    }

    public String getElectrico() {
        return electrico;
    }

    public void setElectrico(String electrico) {
        this.electrico = electrico;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    
}
