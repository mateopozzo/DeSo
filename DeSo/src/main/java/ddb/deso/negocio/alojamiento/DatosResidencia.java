/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.alojamiento;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Clase que almacena la información de la **dirección de residencia**
 * de una persona alojada (huésped o invitado).
 * Incluye calle, número, localidad, provincia, país y código postal.
 * Esta clase es embebible en otras entidades ({@link DatosAlojado}).
 *
 * @author mat
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DatosResidencia {
    /**
     * Nombre de la calle o avenida.
     */
    private String calle;
    /**
     * Número de departamento (opcional).
     */
    private String depto;
    /**
     * Nombre de la localidad o ciudad.
     */
    private String localidad;
    /**
     * Nombre de la provincia o estado.
     */
    private String prov;
    /**
     * Nombre del país.
     */
    private String pais;
    /**
     * Número de la calle.
     */
    private String nro_calle;
    /**
     * Número de piso (opcional).
     */
    private String piso;
    /**
     * Código postal.
     */
    private String cod_post;

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public void setNro_calle(String nro_calle) {
        this.nro_calle = nro_calle;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public void setCod_post(String cod_post) {
        this.cod_post = cod_post;
    }

    public String getCalle() {
        return calle;
    }

    public String getDepto() {
        return depto;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getProv() {
        return prov;
    }

    public String getPais() {
        return pais;
    }

    public String getNro_calle() {
        return nro_calle;
    }

    public String getPiso() {
        return piso;
    }

    public String getCod_post() {
        return cod_post;
    }
    
    
    
    
}
