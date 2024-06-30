package lorenzo.gaston.logic;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * @author LORENZO
 */

@Entity
public class Personal implements Serializable {
    
    @Id
    private int legajo;
    
    private int orden;
    private String apellido;    
    private String nombres;

    private String funcion;
    
    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LicenciasAnuales> vacaciones;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Diagramas> diagramas;

    public Personal() {
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        this.legajo = legajo;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public List<LicenciasAnuales> getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(List<LicenciasAnuales> vacaciones) {
        this.vacaciones = vacaciones;
    }

    public List<Diagramas> getDiagramas() {
        return diagramas;
    }

    public void setDiagramas(List<Diagramas> diagramas) {
        this.diagramas = diagramas;
    }

    public Personal(int legajo, int orden, String apellido, String nombres, String funcion, List<LicenciasAnuales> vacaciones, List<Diagramas> diagramas) {
        this.legajo = legajo;
        this.orden = orden;
        this.apellido = apellido;
        this.nombres = nombres;
        this.funcion = funcion;
        this.vacaciones = vacaciones;
        this.diagramas = diagramas;
    }

    
}
