/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import ddb.deso.TipoDoc;
import java.util.Date;

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
    private long nroDoc;
    private TipoDoc tipoDoc;
    private long CUIT;
    private Date fechanac;

    public DatosPersonales(String nombre, String apellido, String nacionalidad, String posicionIva, String ocupacion, long nroDoc, TipoDoc tipoDoc, long CUIT, Date fechanac) {
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
    
    
}
