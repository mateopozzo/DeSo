package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Data
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reserva> listaReservas;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Estadia> listaEstadias;

    public boolean esMayor() {
        return this.getDatos().getEdad() >= 18;
    }
    public abstract void completarDTO(AlojadoDTO dto);
}