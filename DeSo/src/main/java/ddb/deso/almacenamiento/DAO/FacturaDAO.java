/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.TipoFactura;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import java.util.List;

/**
 * Interfaz de Acceso a Datos (DAO) para la entidad Factura.
 * Define las operaciones CRUD y de búsqueda necesarias para gestionar facturas
 * en la capa de persistencia.
 * * @author mat
 */
public interface FacturaDAO {

    /**
     * Persiste una nueva factura en la base de datos.
     * * @param factura El objeto DTO que contiene los datos de la factura a crear.
     */
    public void crearFactura(FacturaDTO factura);

    /**
     * Actualiza la información de una factura existente.
     * * @param factura El objeto DTO con los datos actualizados.
     */
    public void actualizarFactura(FacturaDTO factura);

    /**
     * Elimina una factura del sistema de persistencia.
     * * @param factura El objeto DTO de la factura que se desea eliminar.
     */
    public void eliminarFactura(FacturaDTO factura);

    /**
     * Recupera una lista con todas las facturas registradas.
     * @return Una lista de {@link FacturaDTO} con todas las facturas,
     * o una lista vacía si no hay registros.
     */
    public List<FacturaDTO> listarFacturas();

    /**
     * Busca una factura específica mediante su número identificador y su tipo.
     * * @param nroFactura El número correlativo de la factura.
     * @param tipo El {@link TipoFactura} de la factura buscada.
     * @return El {@link FacturaDTO} encontrado.
     */
    public FacturaDTO buscarPorNumero(int nroFactura, TipoFactura tipo);
}
