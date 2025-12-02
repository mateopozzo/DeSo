package ddb.deso.alojamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ddb.deso.TipoDoc;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "datos_alojado")
public class DatosAlojado {

    @EmbeddedId
    AlojadoID idAlojado;
    @Embedded
    private DatosContacto datos_contacto;
    @Embedded
    private DatosResidencia datos_residencia;
    @Embedded
    private DatosPersonales datos_personales;

    /* TODO -> revisar usos y debuggear
    public DatosAlojado(){
        var dr = new DatosResidencia();
        var dc = new DatosContacto();
        var dp = new DatosPersonales();
        dp.setAlojadoOwner(this);
        datos_residencia = dr;
        datos_contacto=dc;
        datos_personales = dp;
        checkIns = new ArrayList<>();
        checkOuts = new ArrayList<>();
    }

     */

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
        if(datos_personales!=null) {
            idAlojado = new AlojadoID(personales.getNroDoc(), personales.getTipoDoc());
            this.datos_personales.setAlojadoOwner(this);
        }
    }

    @PostLoad
    private void wireOwnerAfterLoad() {
        if (this.datos_personales != null &&
                this.idAlojado != null &&
                this.idAlojado.getNroDoc() != null &&
                !this.idAlojado.getNroDoc().isEmpty() &&
                this.idAlojado.getTipoDoc() != null
        ) {
            this.datos_personales.setAlojadoOwner(this);
        }
    }
    @Transient
    public void setTipoDoc(TipoDoc tipoDoc) {
        if(this.idAlojado == null) this.idAlojado = new AlojadoID();
        this.idAlojado.setTipoDoc(tipoDoc);
    }
    @Transient
    public TipoDoc getTipoDoc() {
        return (this.idAlojado != null) ? this.idAlojado.getTipoDoc() : null;
    }
    @Transient
    public void setNroDoc(String nroDoc) {
        if(this.idAlojado == null) this.idAlojado = new AlojadoID();
        this.idAlojado.setNroDoc(nroDoc);
    }
    @Transient
    public String getNroDoc() {
        return (this.idAlojado != null) ? this.idAlojado.getNroDoc() : null;
    }

    public void nuevoCheckIn(DatosCheckIn check_in) {
        if(checkIns==null)checkIns=new ArrayList<>();
        checkIns.add(check_in);
    }

    public void nuevoCheckOut(DatosCheckOut check_out) {
        if(checkOuts==null)checkOuts=new ArrayList<>();
        checkOuts.add(check_out);
    }

    public boolean ocupoHabitacion() {
        return ((!checkIns.isEmpty()) || (!checkOuts.isEmpty()));
    }

    public int getEdad() {
        return this.datos_personales.getEdad();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DatosAlojado that = (DatosAlojado) o;
        return Objects.equals(idAlojado, that.idAlojado);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idAlojado);
    }
}