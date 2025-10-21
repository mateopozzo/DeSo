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
}


