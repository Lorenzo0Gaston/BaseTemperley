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
public class Comuna implements Serializable {
    
    @Id
    private String mes;
    
    private String aldia;
    private String adeuda;

    @ManyToOne
    @JoinColumn(name = "personal_legajo")
    private Personal personal;
    
    public Comuna() {
    }

    public Comuna(String mes, String aldia, String adeuda, Personal personal) {
        this.mes = mes;
        this.aldia = aldia;
        this.adeuda = adeuda;
        this.personal = personal;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAldia() {
        return aldia;
    }

    public void setAldia(String aldia) {
        this.aldia = aldia;
    }

    public String getAdeuda() {
        return adeuda;
    }

    public void setAdeuda(String adeuda) {
        this.adeuda = adeuda;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }
    
    
}
