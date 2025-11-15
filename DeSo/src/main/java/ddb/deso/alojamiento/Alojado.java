package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    // --- 1. VUELVE A AGREGAR ESTE CAMPO ---
    @EmbeddedId
    private AlojadoID id;

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

    public Alojado() {
        this.checkIns = new ArrayList<>();
        this.checkOuts = new ArrayList<>();
    }

    @PostLoad
    private void wireOwnerAfterLoad() {
        if (this.datos_personales != null) {
            // Pasa 'this' (Alojado) como el dueño
            this.datos_personales.setAlojadoOwner(this);
        }
    }

    @Transient
    public void setTipoDoc(TipoDoc tipoDoc) {
        if(this.id == null) this.id = new AlojadoID();
        this.id.setTipoDoc(tipoDoc);
    }
    @Transient
    public TipoDoc getTipoDoc() {
        return (this.id != null) ? this.id.getTipoDoc() : null;
    }
    @Transient
    public void setNroDoc(String nroDoc) {
        if(this.id == null) this.id = new AlojadoID();
        this.id.setNroDoc(nroDoc);
    }
    @Transient
    public String getNroDoc() {
        return (this.id != null) ? this.id.getNroDoc() : null;
    }

    public int getEdad() {
        if (this.datos_personales == null) return 0;
        return this.datos_personales.getEdad();
    }

    // --- Métodos abstractos originales ---
    public void checkIn(Alojado alojado) {
    }
    public void checkOut(Alojado alojado) {
    }
    public boolean esMayor() {
        return this.getEdad() >= 18;
    }
    public abstract void completarDTO(AlojadoDTO dto);
}