package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.Huesped;
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

    public PersonaJuridicaDTO(Huesped a){
        this.razonSoc = (a.getRazon_social() == null ? "" : a.getRazon_social());
        this.cuit = a.getDatos().getDatos_personales().getCUIT();
    }

    public PersonaJuridicaDTO(String cuit, String razonSocial) {
        this.cuit = cuit.toString();
        this.razonSoc = razonSocial;
    }
}
