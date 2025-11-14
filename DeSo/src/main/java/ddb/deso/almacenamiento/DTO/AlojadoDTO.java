/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 * DTO aplanado para entidad Alojado
 * * <p>Esta clase se utiliza para transferir datos de un objeto {@code Alojado}
 * (que puede ser un {@code Huesped} o un {@code Invitado}) de forma simplificada y aplanada.
 * Consolida la información de contacto, residencia, personal y de registro
 * (check-in/check-out) en una única estructura, ignorando las jerarquías internas
 * del objeto de dominio.</p>
 * @author mat
 */

@Data
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
    private List<DatosCheckIn> id_check_in;
    private List<DatosCheckOut> id_check_out;

    /**
     * Constructor que inicializa el DTO a partir de una instancia concreta de {@code Alojado}.
     * * <p>Copia todos los datos de contacto, residencia, personales y de registro
     * (check-in/check-out) desde el objeto de dominio {@code Alojado} de origen.</p>
     * * @param i Cualquier instancia concreta de {@code Alojado} ({@code Huesped} o {@code Invitado}).
     */
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
        this.id_check_in=i.getDatos().getCheckIns();
        this.id_check_out=i.getDatos().getCheckOuts();

        // Completar datos segun instancia concreta\
        i.completarDTO(this);
    }

    public AlojadoDTO(Invitado i){
        this((Alojado)i);
    }
    public AlojadoDTO(Huesped h) {
        this((Alojado)h);
    }
    
    public AlojadoDTO() {
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
     * Metodo provisto por IDE Netbeans
     * Genera un código hash basado en el número de documento ({@code nroDoc}) y el tipo de documento ({@code tipoDoc}).
     * * @return El código hash.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.nroDoc);
        hash = 71 * hash + Objects.hashCode(this.tipoDoc);
        return hash;
    }

    /**
     * Compara este {@code AlojadoDTO} con otro objeto para determinar si son iguales.
     * Dos instancias son iguales si tienen mismo hash para número de documento ({@code nroDoc})
     * y tipo de documento ({@code tipoDoc}).
     * * @param obj El objeto a comparar.
     * @return {@code true} si los objetos son iguales, {@code false} en caso contrario.
     */
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
