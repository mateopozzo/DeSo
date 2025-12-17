package ddb.deso.negocio.contabilidad;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "medioPagoId")
public class Cheque extends MedioDePago {

    private LocalDate fechaEmision;
    private String cuit;
    private String titular;
    private Double suma; // En tu diagrama dice 'suma', supongo es el monto

}
