package ddb.deso.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public boolean verificarCamposObligatorios(){
        return  this.getDatos() != null                                            && this.getDatos().getDatos_personales() != null                                 && this.getDatos().getDatos_residencia() != null &&
                this.getDatos().getDatos_personales().getNombre() != null          && !this.getDatos().getDatos_personales().getNombre().isEmpty()                  &&
                this.getDatos().getDatos_personales().getApellido() != null        && !this.getDatos().getDatos_personales().getApellido().isEmpty()                &&
                this.getDatos().getDatos_personales().getTipoDoc() != null         &&
                this.getDatos().getDatos_personales().getNroDoc() != null          && !this.getDatos().getDatos_personales().getNroDoc().isEmpty()                  &&
                this.getDatos().getDatos_personales().getFechanac() != null        && !this.getDatos().getDatos_personales().getFechanac().isAfter(LocalDate.now()) &&
                this.getDatos().getDatos_personales().getNacionalidad() != null    && !this.getDatos().getDatos_personales().getNacionalidad().isEmpty()            &&
                this.getDatos().getDatos_residencia().getCalle() != null           && !this.getDatos().getDatos_residencia().getCalle().isEmpty()                   &&
                this.getDatos().getDatos_residencia().getNro_calle() != null       && !this.getDatos().getDatos_residencia().getNro_calle().isEmpty()               &&
                this.getDatos().getDatos_residencia().getCod_post() != null        && !this.getDatos().getDatos_residencia().getCod_post().isEmpty()                &&
                this.getDatos().getDatos_residencia().getPais() != null            && !this.getDatos().getDatos_residencia().getPais().isEmpty()                    &&
                this.getDatos().getDatos_residencia().getProv() != null            && !this.getDatos().getDatos_residencia().getProv().isEmpty()                    &&
                this.getDatos().getDatos_residencia().getLocalidad() != null       && !this.getDatos().getDatos_residencia().getLocalidad().isEmpty()               &&
                this.getDatos().getDatos_personales().getPosicionIva() != null     && !this.getDatos().getDatos_personales().getPosicionIva().isEmpty()             &&
                this.getDatos().getDatos_personales().getOcupacion() != null       && !this.getDatos().getDatos_personales().getOcupacion().isEmpty();
    }

}