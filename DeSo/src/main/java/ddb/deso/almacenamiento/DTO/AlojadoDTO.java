/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * DTO aplanado para Alojado
 * @author mat
 */

public class AlojadoDTO {
    // Contacto
    private String telefono;
    private String email;
    // Residencia
    private String calle;
    private String depto;
    private String localidad;
    private String prov;
    private String pais;
    private String nro_calle;
    private String piso;
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
    private String fechanac;
    // Si es huesped
    private String razon_social;
    // Check in y out
    private List<Long> id_check_in;
    private List<Long> id_check_out;

    public DatosAlojado getDatos(){
        DatosAlojado datos = new DatosAlojado();

    }

    
    public AlojadoDTO(Alojado i) {
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

        // ingreso/egreso
        this.id_check_in=i.getDatos().getId_check_in();
        this.id_check_out=i.getDatos().getId_check_out();
    }

    public AlojadoDTO(Invitado i){
        this((Alojado)i);
        i.completarDTO(this);
    }
    public AlojadoDTO(Huesped h) {
        this((Alojado)h);
        h.completarDTO(this);
    }
    
    public AlojadoDTO() {
    }

    public List<Long> getId_check_in() {
        return id_check_in;
    }

    public void setId_check_in(List<Long> id_check_in) {
        this.id_check_in = id_check_in;
    }

    public List<Long> getId_check_out() {
        return id_check_out;
    }

    public void setId_check_out(List<Long> id_check_out) {
        this.id_check_out = id_check_out;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public String getTelefono() {
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

    public String getNro_calle() {
        return nro_calle;
    }

    public String getPiso() {
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

    public LocalDate getFechanac() {
        return LocalDate.parse(fechanac);
    }

    public void setTelefono(String telefono) {
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

    public void setNro_calle(String nro_calle) {
        this.nro_calle = nro_calle;
    }

    public void setPiso(String piso) {
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

    public void setFechanac(LocalDate fechanac) {
        this.fechanac = fechanac.toString();
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
