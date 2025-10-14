/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import java.util.Date;
import java.util.Objects;

/**
 *
 * DTO aplanado para Alojado
 * @author mat
 */

public class AlojadoDTO {
    // Contacto
    private long telefono;
    private String email;
    // Residencia
    private String calle;
    private String depto;
    private String localidad;
    private String prov;
    private String pais;
    private int nro_calle;
    private long piso;
    private String cod_post;
    // Persona
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String posicionIva;
    private String ocupacion;
    private String nroDoc;
    private TipoDoc tipoDoc;
    private String CUIT;
    private Date fechanac;
    // Si es huesped
    private String razon_social;

    public AlojadoDTO(Huesped h) {
        // Datos Personales
        this.setNombre(h.getDatos().getDatos_personales().getNombre());
        this.setApellido(h.getDatos().getDatos_personales().getApellido());
        this.setNacionalidad(h.getDatos().getDatos_personales().getNacionalidad());
        this.setPosicionIva(h.getDatos().getDatos_personales().getPosicionIva());
        this.setOcupacion(h.getDatos().getDatos_personales().getOcupacion());
        this.setNroDoc(h.getDatos().getDatos_personales().getNroDoc());
        this.setTipoDoc(h.getDatos().getDatos_personales().getTipoDoc());
        this.setCUIT(h.getDatos().getDatos_personales().getCUIT());
        this.setFechanac(h.getDatos().getDatos_personales().getFechanac());

        // Datos Contacto
        this.setTelefono(h.getDatos().getDatos_contacto().getTelefono());
        this.setEmail(h.getDatos().getDatos_contacto().getEmail());

        // Datos Residencia
        this.setCalle(h.getDatos().getDatos_residencia().getCalle());
        this.setDepto(h.getDatos().getDatos_residencia().getDepto());
        this.setLocalidad(h.getDatos().getDatos_residencia().getLocalidad());
        this.setProv(h.getDatos().getDatos_residencia().getProv());
        this.setPais(h.getDatos().getDatos_residencia().getPais());
        this.setNro_calle(h.getDatos().getDatos_residencia().getNro_calle());
        this.setPiso(h.getDatos().getDatos_residencia().getPiso());
        this.setCod_post(h.getDatos().getDatos_residencia().getCod_post());

        // Si es huesped
        this.setRazon_social(h.getRazon_social());
    }
    
    public AlojadoDTO(Invitado i) {
        // Datos Personales
        this.setNombre(i.getDatos().getDatos_personales().getNombre());
        this.setApellido(i.getDatos().getDatos_personales().getApellido());
        this.setNacionalidad(i.getDatos().getDatos_personales().getNacionalidad());
        this.setPosicionIva(i.getDatos().getDatos_personales().getPosicionIva());
        this.setOcupacion(i.getDatos().getDatos_personales().getOcupacion());
        this.setNroDoc(i.getDatos().getDatos_personales().getNroDoc());
        this.setTipoDoc(i.getDatos().getDatos_personales().getTipoDoc());
        this.setCUIT(i.getDatos().getDatos_personales().getCUIT());
        this.setFechanac(i.getDatos().getDatos_personales().getFechanac());

        // Datos Contacto
        this.setTelefono(i.getDatos().getDatos_contacto().getTelefono());
        this.setEmail(i.getDatos().getDatos_contacto().getEmail());

        // Datos Residencia
        this.setCalle(i.getDatos().getDatos_residencia().getCalle());
        this.setDepto(i.getDatos().getDatos_residencia().getDepto());
        this.setLocalidad(i.getDatos().getDatos_residencia().getLocalidad());
        this.setProv(i.getDatos().getDatos_residencia().getProv());
        this.setPais(i.getDatos().getDatos_residencia().getPais());
        this.setNro_calle(i.getDatos().getDatos_residencia().getNro_calle());
        this.setPiso(i.getDatos().getDatos_residencia().getPiso());
        this.setCod_post(i.getDatos().getDatos_residencia().getCod_post());
    }
    
    public AlojadoDTO() {
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public long getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
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

    public int getNro_calle() {
        return nro_calle;
    }

    public long getPiso() {
        return piso;
    }

    public String getCod_post() {
        return cod_post;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getPosicionIva() {
        return posicionIva;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public TipoDoc getTipoDoc() {
        return tipoDoc;
    }

    public String getCUIT() {
        return CUIT;
    }

    public Date getFechanac() {
        return fechanac;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setNro_calle(int nro_calle) {
        this.nro_calle = nro_calle;
    }

    public void setPiso(long piso) {
        this.piso = piso;
    }

    public void setCod_post(String cod_post) {
        this.cod_post = cod_post;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public void setPosicionIva(String posicionIva) {
        this.posicionIva = posicionIva;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void setCUIT(String CUIT) {
        this.CUIT = CUIT;
    }

    public void setFechanac(Date fechanac) {
        this.fechanac = fechanac;
    }
    /**
     * Metodo provisto por ide
     * 
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nroDoc);
        hash = 71 * hash + Objects.hashCode(this.tipoDoc);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AlojadoDTO other = (AlojadoDTO) obj;
        if (!Objects.equals(this.nroDoc, other.nroDoc)) {
            return false;
        }
        return this.tipoDoc == other.tipoDoc;
    }
    
    
    
}
