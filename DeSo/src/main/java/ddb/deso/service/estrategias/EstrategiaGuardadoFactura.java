package ddb.deso.service.estrategias;

import ddb.deso.almacenamiento.DTO.FacturaDTO;

/**
 * Define el contrato para las implementaciones de persistencia de facturas,
 * actuando como la interfaz <b>Strategy</b> en el patrón de diseño Strategy.
 * <p>
 * Permite encapsular y desacoplar los diferentes algoritmos de generación o
 * almacenamiento de los datos de facturación (e.g., PDF local, Base de Datos, Cloud).
 * </p>
 */
public interface EstrategiaGuardadoFactura {

    /**
     * Ejecuta la lógica concreta de almacenamiento definida por la estrategia.
     *
     * @param factura Objeto de transferencia de datos (DTO) con la información de la factura.
     * @return Representación binaria (byte array) del recurso generado o persistido.
     */
    byte[] guardarFactura(FacturaDTO factura);

}
