package ddb.deso.negocio.contabilidad;

import java.time.LocalDate;

import ddb.deso.negocio.habitaciones.Estadia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Representa un **pago** realizado por un cliente o a un proveedor.
 * <p>
 * Contiene el monto de la transacción, el método utilizado para efectuarla
 * (ej: efectivo, tarjeta) y la fecha en que se procesó.
 * </p>
 *
 * @see MedioDePago
 */

@Getter
@Setter
@Entity
@Table(name="pago")
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    private float monto;
    private LocalDate fecha_pago;

    @OneToOne(fetch=FetchType.LAZY)
    Factura factura;
    @ManyToOne(fetch=FetchType.LAZY)
    ResponsablePago responsable_pago;
    @ManyToOne(fetch=FetchType.LAZY)
    Estadia estadia_pago;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medio_pago_id", referencedColumnName = "id")
    private MedioDePago medioDePago;


    public Pago(float monto, LocalDate fecha_pago) {
        this.monto = monto;
        this.fecha_pago = fecha_pago;
    }

}
