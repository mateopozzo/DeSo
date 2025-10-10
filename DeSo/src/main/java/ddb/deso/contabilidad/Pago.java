package ddb.deso.contabilidad;

import java.util.Date;

import ddb.deso.MedioPago;

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
