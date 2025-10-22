/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import ddb.deso.TipoDoc;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import ddb.deso.TipoDoc;

/**
 *
 * @author mat
 */
public class DatosPersonales {
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String posicionIva;
    private String ocupacion;
    private String nroDoc;
    private TipoDoc tipoDoc;
    private String CUIT;
    private LocalDate fechanac;

    public DatosPersonales(String nombre, String apellido, String nacionalidad, String posicionIva, String ocupacion, String nroDoc, TipoDoc tipoDoc, String CUIT, LocalDate fechanac) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nacionalidad = nacionalidad;
        this.posicionIva = posicionIva;
        this.ocupacion = ocupacion;
        this.nroDoc = nroDoc;
        this.tipoDoc = tipoDoc;
        this.CUIT = CUIT;
        this.fechanac = fechanac;
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
    public String getNroDoc() {
        return nroDoc;
    }
    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }
    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }
    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
    public String getCUIT() {
        return CUIT;
    }
    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }
    public LocalDate getFechanac() {
        return fechanac;
    }
    public void setFechanac(LocalDate fechanac) {
        this.fechanac = fechanac;
    }
     public int getEdad() {
        if (fechanac == null) return 0;
        //LocalDate nacimiento = fechanac.atStartOfDay(ZoneId.systemDefault()).toInstant();
        LocalDate hoy = LocalDate.now();
        return Period.between(fechanac, hoy).getYears();
    }
    
}

    
