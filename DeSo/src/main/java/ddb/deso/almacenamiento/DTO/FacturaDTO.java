/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoFactura;
import ddb.deso.contabilidad.Factura;
import java.util.Date;

/**
 *
 * @author mat
 */
public class FacturaDTO {
    private Date fecha_factura;
    private int num_factura;
    private TipoFactura tipo_factura;
    private float importe_total;
    private float importe_iva;
    private float importe_neto;
    private String destinatario;

    public FacturaDTO(Date fecha_factura, int num_factura, TipoFactura tipo_factura, float importe_total, float importe_iva, float importe_neto, String destinatario) {
        this.fecha_factura = fecha_factura;
        this.num_factura = num_factura;
        this.tipo_factura = tipo_factura;
        this.importe_total = importe_total;
        this.importe_iva = importe_iva;
        this.importe_neto = importe_neto;
        this.destinatario = destinatario;
    }

    public FacturaDTO() {
    }
    
    public FacturaDTO(Factura f) {
                
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

    public void setFecha_factura(Date fecha_factura) {
        this.fecha_factura = fecha_factura;
    }

    public void setNum_factura(int num_factura) {
        this.num_factura = num_factura;
    }

    public void setTipo_factura(TipoFactura tipo_factura) {
        this.tipo_factura = tipo_factura;
    }

    public void setImporte_total(float importe_total) {
        this.importe_total = importe_total;
    }

    public void setImporte_iva(float importe_iva) {
        this.importe_iva = importe_iva;
    }

    public void setImporte_neto(float importe_neto) {
        this.importe_neto = importe_neto;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    
    

}
