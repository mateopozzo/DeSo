package ddb.deso.negocio.contabilidad;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="nota_de_credito")
@NoArgsConstructor
public class NotaCredito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private Float monto_n_credito;

    @ManyToOne(fetch=FetchType.LAZY)
    Factura idFactura;

    public NotaCredito(Float monto_n_credito) {
        this.monto_n_credito = monto_n_credito;
    }
    public Float getMonto_n_credito() {
        return monto_n_credito;
    }
}
