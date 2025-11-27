package ddb.deso.alojamiento;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;

import java.time.LocalDate;

/**
 * Clase factoría estática para la creación de instancias de la clase de dominio
 * {@link ddb.deso.alojamiento.Alojado}, que puede ser de tipo {@link Huesped} o {@link Invitado}.
 * <p>
 * Proporciona métodos para crear objetos {@code Alojado} basados en un tipo
 * predefinido o a partir de un objeto de transferencia de datos (DTO).
 * </p>
 *
 * @author mat
 * @see ddb.deso.alojamiento.Alojado
 * @see Huesped
 * @see Invitado
 */
public class FactoryAlojado {
    public static final int HUESPED=0;
    public static final int INVITADO=1;

    public static int[] tipo = {HUESPED,INVITADO};

    /**
     * Crea una nueva instancia de {@code Alojado} ({@code Huesped} o {@code Invitado})
     * basándose en el tipo especificado.
     *
     * @param tipo El tipo de alojado a crear (usar las constantes {@link #HUESPED}
     * o {@link #INVITADO}).
     * @param datos Un objeto {@link DatosAlojado} que contiene la información
     * base del alojado.
     * @return Una instancia de la clase concreta {@code Alojado} ({@code Huesped} o {@code Invitado}),
     * o {@code null} si el tipo especificado no es válido.
     */
    public static ddb.deso.alojamiento.Alojado create (int tipo, DatosAlojado datos){
        return switch (tipo) {
            case HUESPED -> new Huesped(datos);
            case INVITADO -> new Invitado(datos);
            default -> null;
        };
    }

    /**
     * Convierte un objeto {@code AlojadoDTO} a una instancia de la clase de dominio {@code Alojado}.
     *
     * <p>Actúa como una factoría estática que mapea los datos del DTO a la estructura {@code DatosAlojado}
     * y decide si crear un {@code Huesped} (si {@code razon_social} está presente) o un {@code Invitado} (si no lo está).
     *
     * @param dto El DTO de origen con los datos del alojado.
     * @return Una instancia de {@code Alojado} ({@code Huesped} o {@code Invitado}), o {@code null} si el DTO es nulo.
     * @author mat
     */
    public static ddb.deso.alojamiento.Alojado createFromDTO(AlojadoDTO dto){
        if (dto == null) return null;

        DatosAlojado datos = new DatosAlojado();

        // ==== Datos personales ====
        if (datos.getDatos_personales() != null) {
            datos.getDatos_personales().setNombre(dto.getNombre());
            datos.getDatos_personales().setApellido(dto.getApellido());
            datos.getDatos_personales().setNacionalidad(dto.getNacionalidad());
            datos.getDatos_personales().setPosicionIva(dto.getPosicionIva());
            datos.getDatos_personales().setOcupacion(dto.getOcupacion());
            datos.getDatos_personales().setTipoDoc(dto.getTipoDoc());
            datos.getDatos_personales().setNroDoc(dto.getNroDoc());
            datos.getDatos_personales().setCUIT(dto.getCUIT());
            datos.getDatos_personales().setFechanac(LocalDate.parse(dto.getFechanac()));
        }

        // ==== Datos de contacto ====
        if (datos.getDatos_contacto() != null) {
            datos.getDatos_contacto().setTelefono(dto.getTelefono());
            datos.getDatos_contacto().setEmail(dto.getEmail());
        }

        // ==== Datos de residencia ====
        if (datos.getDatos_residencia() != null) {
            datos.getDatos_residencia().setCalle(dto.getCalle());
            datos.getDatos_residencia().setDepto(dto.getDepto());
            datos.getDatos_residencia().setLocalidad(dto.getLocalidad());
            datos.getDatos_residencia().setProv(dto.getProv());
            datos.getDatos_residencia().setPais(dto.getPais());
            datos.getDatos_residencia().setNro_calle(dto.getNroCalle());
            datos.getDatos_residencia().setPiso(dto.getPiso());
            datos.getDatos_residencia().setCod_post(dto.getCodPost());
        }

        // ==== Check-in / Check-out ====
        datos.setCheckIns(dto.getId_check_in());
        datos.setCheckOuts(dto.getId_check_out());

        ddb.deso.alojamiento.Alojado ret;
        // Si tiene razón social, es huesped
        if (dto.getRazon_social() != null && !dto.getRazon_social().isEmpty()) {
            ret = new Huesped(datos);
            ((Huesped)ret).setRazon_social(dto.getRazon_social());
        } else {
            ret = new Invitado(datos);
        }

        datos.setNroDoc(dto.getNroDoc());
        datos.setTipoDoc(dto.getTipoDoc());
        ret.setId(datos.getIdAlojado());

        return ret;

    }

}


