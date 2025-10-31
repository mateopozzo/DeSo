/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ddb.deso.TipoDoc;

public class DatosAlojado {
    private DatosContacto   datos_contacto;
    private DatosResidencia datos_residencia;
    private DatosPersonales datos_personales;   
    private List<Long> id_check_in;
    private List<Long> id_check_out;
    
    public DatosAlojado(DatosContacto contacto, DatosResidencia residencia, DatosPersonales personales) {
        id_check_in =new ArrayList<Long>();
        id_check_out = new ArrayList<Long>();
        this.datos_contacto = contacto;
        this.datos_residencia = residencia;
        this.datos_personales = personales;
    }
    public DatosAlojado(){
         id_check_in =new ArrayList<Long>();
        id_check_out = new ArrayList<Long>();
        this.datos_contacto=new DatosContacto("","");
        this.datos_personales= new DatosPersonales("","", "",
         "", "","", TipoDoc.DNI,"", LocalDate.now());
         this.datos_residencia= new DatosResidencia("", "", "", "",
          "", "", "", "");
     } 

    public  void setId_check_in(List<Long> id_check_in) {
        this.id_check_in = id_check_in;
    }

    public void setId_check_out(List<Long> id_check_out) {
        this.id_check_out = id_check_out;
    }

    public void nuevoCheckIn(DatosCheckIn check_in){
        id_check_in.add(check_in.getId());
    }
    public void nuevoCheckOut(DatosCheckIn check_out){
        id_check_in.add(check_out.getId());
    }
    public boolean ocupoHabitacion(){
        return ((!id_check_in.isEmpty())||(!id_check_out.isEmpty()));
    }

    public List<Long> getId_check_in() {
        return id_check_in;
    }

    public List<Long> getId_check_out() {
        return id_check_out;
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


