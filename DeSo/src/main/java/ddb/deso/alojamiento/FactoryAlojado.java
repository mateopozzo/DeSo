package ddb.deso.alojamiento;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;

public class FactoryAlojado {
    public static final int HUESPED=0;
    public static final int INVITADO=1;

    public static int[] tipo = {HUESPED,INVITADO};

    public static Alojado create (int tipo, DatosAlojado datos){
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
    public static Alojado createFromDTO(AlojadoDTO dto){
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
            datos.getDatos_personales().setFechanac(dto.getFechanac());
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
            datos.getDatos_residencia().setNro_calle(dto.getNro_calle());
            datos.getDatos_residencia().setPiso(dto.getPiso());
            datos.getDatos_residencia().setCod_post(dto.getCod_post());
        }

        // ==== Check-in / Check-out ====
        datos.setId_check_in(dto.getId_check_in());
        datos.setId_check_out(dto.getId_check_out());

        // --- Tipo de alojado ---
        // Si tiene razón social, es huesped
        if (dto.getRazon_social() != null && !dto.getRazon_social().isEmpty()) {
            Huesped h = new Huesped(datos);
            h.setRazon_social(dto.getRazon_social());
            return h;
        } else {
            return new Invitado(datos);
        }
    }

}


