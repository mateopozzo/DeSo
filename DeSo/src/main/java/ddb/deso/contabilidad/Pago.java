package ddb.deso.contabilidad;

import java.util.Date;

import ddb.deso.habitaciones.Estadia;
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
 * @see ddb.deso.contabilidad.MedioDePago
 */

@Getter
@Setter
@Entity
@Table(name="pago")
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private float monto;
    private Date fecha_pago;

    @OneToOne(fetch=FetchType.EAGER)
    Factura factura;
    @ManyToOne(fetch=FetchType.EAGER)
    ResponsablePago responsable_pago;
    @ManyToOne(fetch=FetchType.EAGER)
    Estadia estadia_pago;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medio_pago_id", referencedColumnName = "id")
    private MedioDePago medioDePago;


    public Pago(float monto, Date fecha_pago) {
        this.monto = monto;
        this.fecha_pago = fecha_pago;
    }

}
