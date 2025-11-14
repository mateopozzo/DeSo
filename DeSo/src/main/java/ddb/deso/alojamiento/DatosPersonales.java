/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import ddb.deso.TipoDoc;
import java.time.LocalDate;
import java.time.Period;
import jakarta.persistence.*;
import lombok.*;

/**
 * Clase que agrupa la información de **identificación personal** de un alojado.
 * Incluye nombre, apellido, documentos, datos fiscales (CUIT, Posición IVA)
 * y fecha de nacimiento.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DatosPersonales {
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String posicionIva;
    private String ocupacion;
    private String CUIT;
    private LocalDate fechanac;
    private String nroDoc;
    private TipoDoc tipoDoc;

    @Transient
    private DatosAlojado alojadoOwner;

    public DatosPersonales(String nombre, String apellido, String nacionalidad,
                           String posicionIva, String nroDoc, TipoDoc tipoDoc, String ocupacion, String CUIT,
                           LocalDate fechanac) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.posicionIva = posicionIva;
        this.ocupacion = ocupacion;
        this.CUIT = CUIT;
        this.fechanac = fechanac;
        this.tipoDoc = tipoDoc;
        this.nroDoc = nroDoc;
        alojadoOwner=null;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getNacionalidad() {
        return nacionalidad;
    }
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
    public String getPosicionIva() {
        return posicionIva;
    }
    public void setPosicionIva(String posicionIva) {
        this.posicionIva = posicionIva;
    }
    public String getOcupacion() {
        return ocupacion;
    }
    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }
    public String getCUIT() {
        return CUIT;
    }
    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }
    public void setAlojadoOwner(DatosAlojado owner) {
        this.alojadoOwner = owner;
        AlojadoID idAlojado = new AlojadoID(nroDoc, tipoDoc);
        owner.setIdAlojado(idAlojado);
        owner.setNroDoc(nroDoc);
        owner.setTipoDoc(tipoDoc);
    }

    public String getNroDoc() {
        if (alojadoOwner == null) {
            return null;
        }
        return alojadoOwner.getNroDoc();
    }

    public void setFechanac(LocalDate f){
        fechanac=f;
    }

    public LocalDate getFechanac() {
        return fechanac;
    }

    public TipoDoc getTipoDoc() {
        if (alojadoOwner == null) {
            return null;
        }
        return alojadoOwner.getTipoDoc();
    }

    public void setNroDoc(String nroDoc) {
        if (alojadoOwner != null) {
            alojadoOwner.setNroDoc(nroDoc);
        }
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        if (alojadoOwner != null) {
            alojadoOwner.setTipoDoc(tipoDoc);
        }
    }
     public int getEdad() {
        if (fechanac == null) return 0;
        //LocalDate nacimiento = fechanac.atStartOfDay(ZoneId.systemDefault()).toInstant();
        LocalDate hoy = LocalDate.now();
        return Period.between(fechanac, hoy).getYears();
    }
}

    
