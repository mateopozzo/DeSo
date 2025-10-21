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
<<<<<<< HEAD
    private DatosContacto   contacto;
    private DatosResidencia residencia;
    private DatosPersonales huesped;   

    public  DatosAlojado(DatosContacto dc, DatosResidencia dr, DatosPersonales dp){
        this.contacto=dc;
        this.residencia=dr;
        this.huesped=dp;
    }
    public DatosContacto getDatosContacto(){
        return this.contacto;
    }
   
    public DatosResidencia getDatosResidencia(){
        return this.residencia;
    }
   
    public DatosPersonales getDatosPersonales(){
        return this.huesped;
    }
=======
    private DatosContacto   datos_contacto;
    private DatosResidencia datos_residencia;
    private DatosPersonales datos_personales;   
    
    public DatosAlojado(DatosContacto contacto, DatosResidencia residencia, DatosPersonales personales) {
        this.datos_contacto = contacto;
        this.datos_residencia = residencia;
        this.datos_personales = personales;
    }

    public DatosContacto getDatos_contacto() {
        return datos_contacto;
    }

    public DatosResidencia getDatos_residencia() {
        return datos_residencia;
    }

    public DatosPersonales getDatos_personales() {
        return datos_personales;
    }

    public void setDatos_contacto(DatosContacto datos_contacto) {
        this.datos_contacto = datos_contacto;
    }

    public void setDatos_residencia(DatosResidencia datos_residencia) {
        this.datos_residencia = datos_residencia;
    }

    public void setDatos_personales(DatosPersonales datos_personales) {
        this.datos_personales = datos_personales;
    }
    
    public int getEdad() {
        return this.datos_personales.getEdad();
    }
    
>>>>>>> 02c0b6a5890096dd1a6ad1cdd911de5593caf083
}


