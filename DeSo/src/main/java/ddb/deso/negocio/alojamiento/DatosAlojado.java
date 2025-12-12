package ddb.deso.negocio.alojamiento;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ddb.deso.negocio.TipoDoc;
import jakarta.persistence.*;
import lombok.*;
/**
 * Clase de entidad que almacena todos los datos de un alojado (huésped o invitado).
 * <p>
 * Esta clase es la representación persistente de los datos personales, de contacto,
 * residencia, y los registros de Check-In y Check-Out asociados.
 * </p>
 *
 * @author mat
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "datos_alojado")
public class DatosAlojado {

    /**
     * Identificador compuesto del alojado (Número y Tipo de Documento).
     */
    @EmbeddedId
    AlojadoID idAlojado;
    /**
     * Datos de contacto del alojado.
     */
    @Embedded
    private DatosContacto datos_contacto;
    /**
     * Datos de residencia del alojado.
     */
    @Embedded
    private DatosResidencia datos_residencia;
    /**
     * Datos de identificación personal del alojado.
     */
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

    /**
     * Lista de registros de Check-In asociados a este alojado.
     */
    @OneToMany(
            mappedBy = "alojado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DatosCheckIn> checkIns;

    /**
     * Lista de registros de Check-Out asociados a este alojado.
     */
    @OneToMany(
            mappedBy = "alojado",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<DatosCheckOut> checkOuts;


    /**
     * Constructor para inicializar un objeto {@code DatosAlojado} con sus datos
     * de contacto, residencia y personales.
     *
     * @param contacto Datos de contacto.
     * @param residencia Datos de residencia.
     * @param personales Datos personales.
     */
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


    /**
     * Método invocado después de que la entidad ha sido cargada desde la base de datos.
     * Su función es asegurar que la referencia bidireccional (owner) en
     * {@link DatosPersonales} esté correctamente configurada.
     */
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

    /**
     * Agrega un nuevo registro de Check-In a la lista de Check-Ins del alojado.
     *
     * @param check_in El objeto {@link DatosCheckIn} a agregar.
     */
    public void nuevoCheckIn(DatosCheckIn check_in) {
        if(checkIns==null)checkIns=new ArrayList<>();
        checkIns.add(check_in);
    }

    /**
     * Agrega un nuevo registro de Check-Out a la lista de Check-Outs del alojado.
     *
     * @param check_out El objeto {@link DatosCheckOut} a agregar.
     */
    public void nuevoCheckOut(DatosCheckOut check_out) {
        if(checkOuts==null)checkOuts=new ArrayList<>();
        checkOuts.add(check_out);
    }

    /**
     * Verifica si el alojado tiene registros de Check-In o Check-Out,
     * indicando si alguna vez ocupó una habitación.
     *
     * @return {@code true} si tiene registros de Check-In o Check-Out, {@code false} en caso contrario.
     */
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