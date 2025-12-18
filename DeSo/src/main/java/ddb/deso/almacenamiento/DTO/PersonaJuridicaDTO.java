package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * DTO de persona juridica, utilizado en CU07 cuando se cobra a un tercero
 */
@NoArgsConstructor
@Getter
@Setter
public class PersonaJuridicaDTO {
    private String razonSoc;
    private String cuit;
    private String tipoResponsable; // "EMPRESA" o "HUESPED"

    public PersonaJuridicaDTO(String nombreCompleto, String cuit, String tipoResponsable) {
        this.razonSoc = nombreCompleto;
        this.cuit = cuit;
        this.tipoResponsable = tipoResponsable;
    }

    public PersonaJuridicaDTO(ResponsablePago r) {
        this.razonSoc = r.getRazonSocial();
        this.cuit = String.valueOf(r.getCuit());
    }
}
