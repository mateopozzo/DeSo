/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoFactura;
import ddb.deso.contabilidad.Factura;
import java.util.List;

/**
 *
 * @author mat
 */
public interface FacturaDAO {
    void crearFactura(FacturaDTO factura);
    void actualizarFactura(FacturaDTO factura);
    void eliminarFactura(FacturaDTO fatura);
    List<FacturaDTO> listarFacturas();
    FacturaDTO buscarPorNumero(int nroFactora, TipoFactura tipo);
}
