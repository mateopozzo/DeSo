/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosCheckIn;

import java.util.List;

/**
 * Contrato de persistencia para la entidad {@link DatosCheckIn}.
 * Define operaciones CRUD y consultas especializadas sobre el repositorio de datos.
 */
public interface DatosCheckInDAO {
    /**
     * Metodo de creacion persistente de entidad check-in
     *
     * @param datosCheckIn
     */
    void crearDatosCheckIn(DatosCheckIn datosCheckIn);

    /**
     * Metodo de modificacion de entidad check-in persistida
     *
     * @param datosCheckInPre
     * @param datosCheckIn
     */
    void actualizarDatosCheckIn(DatosCheckIn datosCheckInPre, DatosCheckIn datosCheckIn);

    /**
     * Metodo de eliminacion de registro check-in asociado a datosCheckIn
     *
     * @param datosCheckIn
     */
    void eliminarDatosCheckIn(DatosCheckIn datosCheckIn);

    /**
     * Devuelve una lista con todos los registros de CheckIn en la base de datos
     *
     * @return
     */
    List<DatosCheckIn> listarDatosCheckIn();
}
