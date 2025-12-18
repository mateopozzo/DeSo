/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import java.util.List;

/**
 * Contrato de persistencia para la entidad {@link DatosCheckOut}.
 * Define operaciones CRUD y consultas especializadas sobre el repositorio de datos.
 */
public interface DatosCheckOutDAO {

    /**
     * Metodo de creacion persistente de entidad check-in
     *
     * @param datosCheckOut
     */
    void crearDatosCheckOut(DatosCheckOut datosCheckOut);

    /**
     * Metodo de modificacion de entidad check-in persistida
     *
     * @param datosCheckOut
     */
    void actualizarDatosCheckOut(DatosCheckOut datosCheckOut);

    /**
     * Metodo de eliminacion de registro check-in asociado a datosCheckIn
     *
     * @param datosCheckOut
     */
    void eliminarDatosCheckOut(DatosCheckOut datosCheckOut);

    /**
     * Devuelve una lista con todos los registros de CheckIn en la base de datos asociados a un Alojado
     *
     * @param documento : Numero de documento del alojado
     * @param tipo : Tipo de documento
     * @return lista de entidades CheckOut persistidas
     */
    List<DatosCheckOut> listarDatosCheckOut(String documento, TipoDoc tipo);
}
