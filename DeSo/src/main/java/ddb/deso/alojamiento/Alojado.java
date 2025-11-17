package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    @EmbeddedId
    private AlojadoID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @MapsId("id")
    @JoinColumns({
            @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
            @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
    })
    protected DatosAlojado datos;

    public AlojadoID getId() {
        return id;
    }

    public void setId(AlojadoID id) {
        this.id = id;
    }

    public DatosAlojado getDatos() {
        return datos;
    }

    public void setDatos(DatosAlojado datos) {
        this.datos = datos;
    }

    public void checkIn(Alojado alojado) {
    }
    public void checkOut(Alojado alojado) {
    }
    public boolean esMayor() {
        return this.getEdad() >= 18;
    }
    public abstract void completarDTO(AlojadoDTO dto);
}