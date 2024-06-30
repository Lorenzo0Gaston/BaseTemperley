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
public class LicenciasAnuales implements Serializable {
    
    @Id
    private String mes;
    
    private String inicia;
    private String finaliza;
    private String vuelve;
    
    @ManyToOne
    @JoinColumn(name = "persona_legajo")
    private Personal personal;

    public LicenciasAnuales() {
    }

    public LicenciasAnuales(String mes, String inicia, String finaliza, String vuelve, Personal persona) {
        this.mes = mes;
        this.inicia = inicia;
        this.finaliza = finaliza;
        this.vuelve = vuelve;
        this.personal = persona;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getInicia() {
        return inicia;
    }

    public void setInicia(String inicia) {
        this.inicia = inicia;
    }

    public String getFinaliza() {
        return finaliza;
    }

    public void setFinaliza(String finaliza) {
        this.finaliza = finaliza;
    }

    public String getVuelve() {
        return vuelve;
    }

    public void setVuelve(String vuelve) {
        this.vuelve = vuelve;
    }

    public Personal getPersona() {
        return personal;
    }

    public void setPersona(Personal persona) {
        this.personal = persona;
    }

        
}
