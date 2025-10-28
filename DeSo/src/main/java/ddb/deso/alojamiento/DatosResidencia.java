/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

public class DatosResidencia {
    private String calle;
    private String depto;
    private String localidad;
    private String prov;
    private String pais;
    private String nro_calle;
    private String piso;
    private String cod_post;

    public DatosResidencia(String calle, String depto, String localidad, String prov, String pais, String nro_calle, String piso, String cod_post) {
        this.calle = calle;
        this.depto = depto;
        this.localidad = localidad;
        this.prov = prov;
        this.pais = pais;
        this.nro_calle = nro_calle;
        this.piso = piso;
        this.cod_post = cod_post;
    }

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
