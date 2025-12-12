package ddb.deso.negocio.alojamiento;/* ... (paquete e importaciones) ... */

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Reserva;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase base abstracta que representa a una persona **alojada** en el sistema.
 * <p>
 * Utiliza la estrategia de herencia {@code SINGLE_TABLE} con un discriminador
 * para distinguir entre subtipos (e.g., {@link Huesped}, {@link Invitado}).
 * Contiene los datos comunes y la gestión de reservas y estadías.
 * </p>
 *
 * @author mat
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "alojado")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_alojado", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojado {

    /**
     * Constructor para inicializar un objeto Alojado con sus datos básicos.
     *
     * @param da Objeto {@link DatosAlojado} que contiene la información personal,
     * contacto y residencia.
     */
    public Alojado(DatosAlojado da){
        this.datos = da;
        this.id = this.getDatos().getIdAlojado();
        this.listaEstadias=new ArrayList<>();
        this.listaReservas=new ArrayList<>();
    }

    /**
     * Clave primaria compuesta embebida, obtenida de los datos del alojado.
     */
    @EmbeddedId
    private AlojadoID id;

    /**
     * Datos generales del alojado (personales, contacto, residencia, etc.).
     * La relación es {@code OneToOne} y mapea los datos al ID ({@code MapsId}).
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId("id")
    @JoinColumns({
            @JoinColumn(name = "nro_doc", referencedColumnName = "nro_doc"),
            @JoinColumn(name = "tipo_doc", referencedColumnName = "tipo_doc")
    })
    protected DatosAlojado datos;

    /**
     * Lista de {@link Reserva}s asociadas a este alojado.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reserva> listaReservas;

    /**
     * Lista de {@link Estadia}s asociadas a este alojado.
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Estadia> listaEstadias;

    public boolean esMayor() {
        return this.getDatos().getEdad() >= 18;
    }

    /**
     * Método abstracto para completar un DTO específico con los datos propios
     * del subtipo concreto ({@link Huesped} o {@link Invitado}).
     *
     * @param dto El DTO (Data Transfer Object) a completar.
     */
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


    /**
     * Verifica que todos los campos obligatorios del alojado estén presentes y no vacíos.
     * Incluye validaciones para datos personales, de residencia y la fecha de nacimiento.
     *
     * @return {@code true} si todos los campos obligatorios están completos, {@code false} en caso contrario.
     */
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