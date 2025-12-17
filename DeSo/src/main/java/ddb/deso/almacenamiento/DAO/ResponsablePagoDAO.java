/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.contabilidad.ResponsablePago;
import java.util.List;

/**
 *
 * @author mat
 */
public interface ResponsablePagoDAO {
    void crear(ResponsablePago responsablePago);
    void actualizar(ResponsablePago responsablePago);
    void eliminar(ResponsablePago responsablePago);
    List<ResponsablePago> listar();
    ResponsablePago buscarPorCUIT(String CUIT);

    /**
     * Busca un responsable de pago por su ID (CUIT).
     * @param id El CUIT del responsable (clave primaria).
     * @return La entidad encontrada o null.
     */
    ResponsablePago read(Long id);
}
