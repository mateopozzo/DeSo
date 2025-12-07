package ddb.deso.service.contabilidad;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "medioPagoId")
public class Efectivo extends MedioDePago {

    private String nacionalidad; // Según tu diagrama

}
