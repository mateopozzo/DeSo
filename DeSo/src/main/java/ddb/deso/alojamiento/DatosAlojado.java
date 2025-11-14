/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;

import java.util.ArrayList;
import java.util.List;

import ddb.deso.TipoDoc;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@Table(name = "datos_alojado")
@NoArgsConstructor
public class DatosAlojado {
    @EmbeddedId
    AlojadoID idAlojado;
    @Embedded
    private DatosContacto datos_contacto;
    @Embedded
    private DatosResidencia datos_residencia;
    @Embedded
    private DatosPersonales datos_personales;

    @OneToMany(
            mappedBy = "alojado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DatosCheckIn> checkIns;

    @OneToMany(
            mappedBy = "alojado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DatosCheckOut> checkOuts;

    public DatosAlojado(DatosContacto contacto, DatosResidencia residencia, DatosPersonales personales) {
        checkOuts = new ArrayList<>();
        checkIns = new ArrayList<>();
        this.datos_contacto = contacto;
        this.datos_residencia = residencia;
        this.datos_personales = personales;
        if(datos_personales!=null)this.datos_personales.setAlojadoOwner(this);
    }

    @PostLoad
    private void wireOwnerAfterLoad() {
        if (this.datos_personales != null) {
            this.datos_personales.setAlojadoOwner(this);
        }
    }

    public void setTipoDoc(TipoDoc tipoDoc) {
        if(this.idAlojado == null) this.idAlojado = new AlojadoID();
        this.idAlojado.setTipoDoc(tipoDoc);
    }
    public TipoDoc getTipoDoc() {
        return (this.idAlojado != null) ? this.idAlojado.getTipoDoc() : null;
    }
    public void setNroDoc(String nroDoc) {
        if(this.idAlojado == null) this.idAlojado = new AlojadoID();
        this.idAlojado.setNroDoc(nroDoc);
    }
    public String getNroDoc() {
        return (this.idAlojado != null) ? this.idAlojado.getNroDoc() : null;
    }

    public void nuevoCheckIn(DatosCheckIn check_in) {
        checkIns.add(check_in);
    }

    public void nuevoCheckOut(DatosCheckOut check_out) {
        checkOuts.add(check_out);
    }

    public boolean ocupoHabitacion() {
        return ((!checkIns.isEmpty()) || (!checkOuts.isEmpty()));
    }


    public DatosContacto getDatos_contacto() {
        return datos_contacto;
    }

    public DatosResidencia getDatos_residencia() {
        return datos_residencia;
    }

    public DatosPersonales getDatos_personales() {
        return datos_personales;
    }

    public void setDatos_contacto(DatosContacto datos_contacto) {
        this.datos_contacto = datos_contacto;
    }

    public void setDatos_residencia(DatosResidencia datos_residencia) {
        this.datos_residencia = datos_residencia;
    }

    public void setDatos_personales(DatosPersonales datos_personales) {
        this.datos_personales = datos_personales;
    }

    public int getEdad() {
        return this.datos_personales.getEdad();
    }

}


