/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.habitaciones.Servicio;
import java.util.List;

/**
 * Interfaz de Acceso a Datos (DAO) para la entidad Servicio.
 *
 */
public interface ServicioDAO {

    /**
     * Almacena un registro nuevo de una entidad de {@link Servicio}
     * @param servicio : Entidad de servicio creada
     */
    void crear(Servicio servicio);

    /**
     * Actualiza un registro presernte en el sistema de persistencia de {@link Servicio}
     * @param servicio : Entidad de servicio creada
     */
    void actualizar(Servicio servicio);

    /**
     * Elimina un registro presernte en el sistema de persistencia de {@link Servicio}
     * @param servicio : Entidad de servicio creada
     */
    void eliminar(Servicio servicio);

    /**
     * Recupera todas las entradas de {@link Servicio} persistidas
     * @return : una lista de {@link Servicio} con todos los servicios que presta el hotel
     */
    List<Servicio> listar();
}
