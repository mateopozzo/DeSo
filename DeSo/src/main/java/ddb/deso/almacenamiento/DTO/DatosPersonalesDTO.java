package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoDoc;
import java.time.LocalDate;

public class DatosPersonalesDTO {
    private String nombre;
    private String apellido;
    private String nroDoc;
    private TipoDoc tipoDoc;

    /*
    Yo no uso estos atributos pero no sé si es correcto sacarlos.
    private String posicionIva;
    private String ocupacion;
    private String nacionalidad;
    private String CUIT;
    private LocalDate fechanac;*/

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

    public DatosPersonalesDTO(String nombre, String apellido, TipoDoc tipoDoc, String nroDoc) {
        this.setApellido(apellido);
        this.setNombre(nombre);
        this.setNroDoc(nroDoc);
        this.setTipoDoc(tipoDoc);
    }
}


