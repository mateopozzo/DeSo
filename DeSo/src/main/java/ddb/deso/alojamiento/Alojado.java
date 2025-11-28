package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    public Alojado(DatosAlojado da){
        this.datos = da;
        this.id = this.getDatos().getIdAlojado();
        this.listaEstadias=new ArrayList<>();
        this.listaReservas=new ArrayList<>();
    }

    @EmbeddedId
    private AlojadoID id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Alojado alojado = (Alojado) o;
        return Objects.equals(id, alojado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


}