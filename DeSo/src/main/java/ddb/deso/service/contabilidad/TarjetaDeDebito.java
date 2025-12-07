package ddb.deso.service.contabilidad;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@PrimaryKeyJoinColumn(name = "medioPagoId")
public class TarjetaDeDebito extends MedioDePago {

    private String pan;
    private LocalDate vencimiento;
    private String titular;

}