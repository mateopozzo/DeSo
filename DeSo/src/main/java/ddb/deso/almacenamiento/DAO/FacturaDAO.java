/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.TipoFactura;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import java.util.List;

/**
 *
 * @author mat
 */
public interface FacturaDAO {
    public void crearFactura(FacturaDTO factura);
    public void actualizarFactura(FacturaDTO factura);
    public void eliminarFactura(FacturaDTO fatura);
    public List<FacturaDTO> listarFacturas();
    public FacturaDTO buscarPorNumero(int nroFactura, TipoFactura tipo);
}
