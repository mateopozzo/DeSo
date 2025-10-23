/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DTO;

import ddb.deso.TipoFactura;
import ddb.deso.contabilidad.Factura;
import java.util.Date;
import java.util.Objects;

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
        this.fecha_factura = f.getFecha_factura();
        this.num_factura = f.getNum_factura();
        this.tipo_factura = f.getTipo_factura();
        this.importe_total = f.getImporte_total();
        this.importe_iva = f.getImporte_iva();
        this.importe_neto = f.getImporte_neto();
        this.destinatario = f.getDestinatario();
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

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacturaDTO other = (FacturaDTO) obj;
        if (this.num_factura != other.num_factura) {
            return false;
        }
        if (Float.floatToIntBits(this.importe_total) != Float.floatToIntBits(other.importe_total)) {
            return false;
        }
        if (Float.floatToIntBits(this.importe_iva) != Float.floatToIntBits(other.importe_iva)) {
            return false;
        }
        if (Float.floatToIntBits(this.importe_neto) != Float.floatToIntBits(other.importe_neto)) {
            return false;
        }
        if (!Objects.equals(this.destinatario, other.destinatario)) {
            return false;
        }
        if (!Objects.equals(this.fecha_factura, other.fecha_factura)) {
            return false;
        }
        return this.tipo_factura == other.tipo_factura;
    }
    
    
    

}
