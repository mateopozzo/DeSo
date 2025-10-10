/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

/**
 *
 * @author mat
 */
public class DatosAlojado {
    private DatosContacto   contacto;
    private DatosResidencia residencia;
    private DatosPersonales huesped;   
    
    public DatosAlojado(DatosContacto contacto, DatosResidencia residencia, DatosPersonales huesped) {
        this.contacto = contacto;
        this.residencia = residencia;
        this.huesped = huesped;
    }
    public DatosContacto getContacto() {
        return contacto;
    }
    public void setContacto(DatosContacto contacto) {
        this.contacto = contacto;
    }
    public DatosResidencia getResidencia() {
        return residencia;
    }
    public void setResidencia(DatosResidencia residencia) {
        this.residencia = residencia;
    }
    public DatosPersonales getHuesped() {
        return huesped;
    }
    public void setHuesped(DatosPersonales huesped) {
        this.huesped = huesped;
    }
    public int getEdad() {
        return this.huesped.getEdad();
    }
    
}
