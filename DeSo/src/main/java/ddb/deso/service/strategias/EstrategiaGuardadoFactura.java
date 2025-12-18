package ddb.deso.service.strategias;

import ddb.deso.almacenamiento.DTO.FacturaDTO;

public interface EstrategiaGuardadoFactura {

    byte[] guardarFactura(FacturaDTO factura);

}
