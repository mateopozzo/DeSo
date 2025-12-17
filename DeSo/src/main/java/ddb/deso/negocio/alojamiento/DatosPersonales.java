/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.alojamiento;

import ddb.deso.negocio.TipoDoc;
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
@Setter
@Getter
public class DatosPersonales {
    /**
     * Nombre del alojado.
     */
    private String nombre;
    /**
     * Apellido del alojado.
     */
    private String apellido;
    /**
     * Nacionalidad del alojado.
     */
    private String nacionalidad;
    /**
     * Posición fiscal del alojado frente al IVA.
     */
    private String posicionIva;
    /**
     * Ocupación actual del alojado.
     */
    private String ocupacion;
    /**
     * Clave Única de Identificación Tributaria o similar (CUIT/CUIL).
     */
    private String CUIT;
    /**
     * Fecha de nacimiento del alojado.
     */
    private LocalDate fechanac;
    /**
     * Número de documento (transient, se gestiona a través de {@link DatosAlojado}).
     */
    @Transient
    private String nroDoc;
    /**
     * Tipo de documento (transient, se gestiona a través de {@link DatosAlojado}).
     */
    @Transient
    private TipoDoc tipoDoc;

    /**
     * Referencia al objeto {@link DatosAlojado} propietario de esta instancia,
     * usado para mantener la coherencia con el {@code EmbeddedId} (transient).
     */
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


    public void setAlojadoOwner(DatosAlojado owner) {
        this.alojadoOwner = owner;
        AlojadoID idAlojado = new AlojadoID(owner.getNroDoc(), owner.getTipoDoc());
        this.setNroDoc(idAlojado.getNroDoc());
        this.setTipoDoc(owner.getTipoDoc());
    }

    public String getNroDoc() {
        if(alojadoOwner!=null)
            return alojadoOwner.getNroDoc();
        return this.nroDoc;
    }

    public TipoDoc getTipoDoc() {
        if(alojadoOwner!=null)
            return alojadoOwner.getTipoDoc();
        return this.tipoDoc;
    }

    public void setNroDoc(String nroDoc) {
        if (alojadoOwner != null) {
            alojadoOwner.setNroDoc(nroDoc);
            alojadoOwner.getIdAlojado().setNroDoc(nroDoc);
        }
        this.nroDoc=nroDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        if (alojadoOwner != null) {
            alojadoOwner.getIdAlojado().setTipoDoc(tipoDoc);
            alojadoOwner.setTipoDoc(tipoDoc);
        }
        this.tipoDoc = tipoDoc;
    }

    public int getEdad() {
        if (fechanac == null) return 0;
        //LocalDate nacimiento = fechanac.atStartOfDay(ZoneId.systemDefault()).toInstant();
        LocalDate hoy = LocalDate.now();
        return Period.between(fechanac, hoy).getYears();
    }
}

    
