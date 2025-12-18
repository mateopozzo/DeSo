package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.Huesped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PersonaJuridica {
    private String razonSoc;
    private String cuit;

    public PersonaJuridica(Huesped a){
        this.razonSoc = (a.getRazon_social() == null ? "" : a.getRazon_social());
        this.cuit = a.getDatos().getDatos_personales().getCUIT();
    }
}
