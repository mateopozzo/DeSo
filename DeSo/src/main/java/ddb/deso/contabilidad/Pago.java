package ddb.deso.contabilidad;

import java.util.Date;

import ddb.deso.MedioPago;


/**
 * Representa un **pago** realizado por un cliente o a un proveedor.
 * <p>
 * Contiene el monto de la transacción, el método utilizado para efectuarla
 * (ej: efectivo, tarjeta) y la fecha en que se procesó.
 * </p>
 *
 * @see ddb.deso.MedioPago
 */
public class Pago {
    private float monto;
    private MedioPago medio_pago;
    private Date fecha_pago;

    public Pago(float monto, MedioPago medio_pago, Date fecha_pago) {
        this.monto = monto;
        this.medio_pago = medio_pago;
        this.fecha_pago = fecha_pago;
    }
    public float getMonto() {
        return monto;
    }
    public MedioPago getMedio_pago() {
        return medio_pago;
    }
    public Date getFecha_pago() {
        return fecha_pago;
    }

}
