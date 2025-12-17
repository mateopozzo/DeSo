package ddb.deso.negocio;
/**
 * Enumeración que define los **tipos de factura** admitidos para la emisión
 * de comprobantes fiscales.
 *
 * @author mat
 */
public enum TipoFactura {
    /** Factura tipo A: para responsables inscriptos (con IVA discriminado). */
    A,
    /** Factura tipo B: para consumidores finales (con IVA incluido). */
    B,
    /** Factura tipo C: para Monotributistas o exentos. */
    C,
    /** Factura tipo E: para exportaciones. */
    E
}
