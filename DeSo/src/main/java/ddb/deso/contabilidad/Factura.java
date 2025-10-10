package ddb.deso.contabilidad;

import java.util.Date;

import ddb.deso.TipoFactura;

public class Factura {
    private Date fecha_factura;
    private int num_factura;
    private TipoFactura tipo_factura;
    private float importe_total;
    private float importe_iva;
    private float importe_neto;
    private String destinatario;

    public Factura(Date fecha_factura, int num_factura, TipoFactura tipo_factura, float importe_total, float importe_iva, float importe_neto, String destinatario) {
        this.fecha_factura = fecha_factura;
        this.num_factura = num_factura;
        this.tipo_factura = tipo_factura;
        this.importe_total = importe_total;
        this.importe_iva = importe_iva;
        this.importe_neto = importe_neto;
        this.destinatario = destinatario;
    }
    public Date getFecha_factura() {
        return fecha_factura;
    }
    public int getNum_factura() {
        return num_factura;
    }
    public TipoFactura getTipo_factura() {
        return tipo_factura;
    }
    public float getImporte_total() {
        return importe_total;
    }
    public float getImporte_iva() {
        return importe_iva;
    }
    public float getImporte_neto() {
        return importe_neto;
    }
    public String getDestinatario() {
        return destinatario;
    }


}
