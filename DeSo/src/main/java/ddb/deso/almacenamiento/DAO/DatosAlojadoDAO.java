/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.almacenamiento.DAO;

import ddb.deso.service.TipoDoc;
import ddb.deso.service.alojamiento.DatosAlojado;

/**
 * Interfaz DAO responsable del acceso y manipulación de los datos
 * personales asociados a un huésped.
 * <p>
 * Define las operaciones CRUD sobre los objetos {@link DatosAlojado},
 * que representan la información detallada de cada persona alojada.
 * </p>
 *
 * @author mat
 */
public interface DatosAlojadoDAO {

    /**
     * Crea un nuevo registro de datos personales del huésped.
     *
     * @param datosAlojado objeto {@link DatosAlojado} con los datos a almacenar.
     */
    void crearDatosAlojado(DatosAlojado datosAlojado);

    /**
     * Actualiza un registro existente de datos del huésped.
     *
     * @param datosAlojado objeto {@link DatosAlojado} con la información modificada.
     */
    void actualizarDatosAlojado(DatosAlojado datosAlojado);

     /**
     * Elimina un registro de datos personales del huésped.
     *
     * @param datosAlojado objeto {@link DatosAlojado} que identifica los datos a eliminar.
     */
    void eliminarDatosAlojado(DatosAlojado datosAlojado);

    /**
     * Busca los datos personales de un huésped a partir de su documento.
     *
     * @param documento número de documento del huésped.
     * @param tipo tipo de documento (DNI, Pasaporte, etc.).
     * @return el objeto {@link DatosAlojado} correspondiente si se encuentra, o {@code null} si no existe.
     */
    DatosAlojado buscarDatosAlojado(String documento, TipoDoc tipo);
}
