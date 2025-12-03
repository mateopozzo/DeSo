package ddb.deso.contabilidad;

import java.time.LocalDate;
import java.util.List;

import ddb.deso.TipoFactura;
import ddb.deso.contabilidad.NotaCredito;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Representa un **documento contable** o factura.
 * <p>
 * Contiene todos los detalles fiscales y monetarios de una transacción,
 * incluyendo fechas, importes desglosados (neto, IVA, total) y el destinatario.
 * </p>
 *
 * @see ddb.deso.TipoFactura
 */

@Getter
@Setter
@Entity
@Table(name="factura")
@NoArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private LocalDate fecha_factura;
    private int num_factura;
    private TipoFactura tipo_factura;
    private float importe_total;
    private float importe_iva;
    private float importe_neto;
    private String destinatario;

    @OneToMany(fetch=FetchType.LAZY)
    List<NotaCredito> notaCredito;

    public Factura(LocalDate fecha_factura, int num_factura, TipoFactura tipo_factura, float importe_total, float importe_iva, float importe_neto, String destinatario) {
        this.fecha_factura = fecha_factura;
        this.num_factura = num_factura;
        this.tipo_factura = tipo_factura;
        this.importe_total = importe_total;
        this.importe_iva = importe_iva;
        this.importe_neto = importe_neto;
        this.destinatario = destinatario;
    }


}
