/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mat
 */
public class DatosAlojado {
    private DatosContacto   datos_contacto;
    private DatosResidencia datos_residencia;
    private DatosPersonales datos_personales;   
    private List<DatosCheckIn> datos_check_in;
    private List<DatosCheckOut> datos_check_out;
    
    public DatosAlojado(DatosContacto contacto, DatosResidencia residencia, DatosPersonales personales) {
        datos_check_out =new ArrayList<DatosCheckOut>();
        datos_check_in = new ArrayList<DatosCheckIn>();
        this.datos_contacto = contacto;
        this.datos_residencia = residencia;
        this.datos_personales = personales;
    }
    
    public nuevoCheckIn(DatosCheckIn){
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
    
}


